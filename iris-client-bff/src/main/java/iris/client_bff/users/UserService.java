package iris.client_bff.users;

import static dev.samstevens.totp.util.Utils.*;
import static iris.client_bff.users.UserRole.*;
import static java.util.Objects.*;
import static org.apache.commons.lang3.StringUtils.*;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import io.vavr.control.Try;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.users.UserAccount.UserAccountBuilder;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	public static final String APP_NAME = "IRIS Client";

	private final UserAccountsRepository users;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticatedUserAware authManager;
	private final SecretGenerator secretGenerator;
	private final QrDataFactory qrDataFactory;
	private final QrGenerator qrGenerator;
	private final AlertService alertService;
	private final CodeVerifier codeVerifier;

	public Optional<UserAccount> findByUuid(UUID id) {
		return users.findById(UserAccountIdentifier.of(id));
	}

	public Optional<UserAccount> findByUsername(String username) {
		return users.findUserByUsername(username);
	}

	public boolean isOldPasswordCorrect(@NonNull UserAccountIdentifier id, @NonNull String oldPassword) {

		return users.findUserById(id)
				.map(UserAccount::getPassword)
				.filter(it -> passwordEncoder.matches(oldPassword, it))
				.isPresent();
	}

	public List<UserAccount> loadAll() {
		return users.findAllUsers();
	}

	public UserAccount create(UserAccount user) {
		return create(user, false);
	}

	public UserAccount create(UserAccount user, boolean useMfa) {

		log.info("Create user {}", user.getUserName());

		return users.save(updateUseMfa(user, useMfa));
	}

	public UserAccount update(UserAccountIdentifier userId, @Nullable String lastName, @Nullable String firstName,
			@Nullable String userName, @Nullable String password, @Nullable UserRole role, @Nullable Boolean locked,
			Boolean useMfa) {

		log.info("Update user: {}", userId);

		var userAccount = loadUser(userId);

		var isAdmin = authManager.isAdmin();
		var oldUserName = userAccount.getUserName();
		var invalidateTokens = false;

		if (!isAdmin && !isItCurrentUser(userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change other users!");
		}

		if (nonNull(lastName)) {
			userAccount.setLastName(lastName);
		}

		if (nonNull(firstName)) {
			userAccount.setFirstName(firstName);
		}

		if (!isAdmin && isNotBlank(userName)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his username!");
		}

		if (isNotBlank(userName)
				&& !StringUtils.equals(oldUserName, userName)) {

			userAccount.setUserName(userName);
			invalidateTokens = true;
		}

		if (role != null) {

			if (!isAdmin) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A non admin user can't change his role!");
			}

			if (isRemoveLastAdmin(userAccount, role)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role of the last admin can't be changed!");
			}

			if (userAccount.getRole() != role) {
				userAccount.setRole(role);
				invalidateTokens = true;
			}
		}

		if (isNotBlank(password) && !passwordEncoder.matches(password, userAccount.getPassword())) {

			userAccount.setPassword(passwordEncoder.encode(password));
			invalidateTokens = true;
		}

		if (locked != null) {
			var changed = updateLockState(userAccount, locked);
			invalidateTokens = changed || invalidateTokens;
		}

		if (useMfa != null) {
			updateUseMfa(userAccount, useMfa);
		}

		if (invalidateTokens) {
			userAccount.markLoginIncompatiblyUpdated();
		}

		return users.save(userAccount);
	}

	private boolean updateLockState(UserAccount userAccount, boolean locked) {

		if (!authManager.isAdmin()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Only an admin user can change the lock state of an user!");
		}

		if (isItCurrentUser(userAccount.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user may not change his own lock state!");
		}

		if (userAccount.isLocked() == locked) {
			return false;
		}

		userAccount.setLocked(locked);
		return true;
	}

	public void deleteById(UserAccountIdentifier id) {

		if (isItCurrentUser(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user may not delete himself!");
		}

		users.findUserById(id)
				.ifPresent(account -> {

					log.info("Delete User: {}", id);

					users.save(account.markDeleted());
				});
	}

	public boolean isItCurrentUser(UserAccountIdentifier userId) {
		return authManager.getCurrentUser()
				.map(UserAccount::getId)
				.filter(it -> it.equals(userId))
				.isPresent();
	}

	/**
	 * If the user does not exist yet, it will be created.
	 * <p/>
	 * Default values are included:
	 * <ul>
	 * <li>`userName` = value of `{username}`</li>
	 * <li>`password` = random UUID</li>
	 * <li>`role` = `ANONYMOUS`</li>
	 * <li>`locked` = `false`</li>
	 * <li>`deletedAt` = null</li>
	 * <li>`useMfa` = `false`</li>
	 * </ul>
	 *
	 * @param username
	 * @param defineUserFunction
	 * @return The found or newly created user.
	 */
	public UserAccount findOrCreateUser(String username, UnaryOperator<UserAccountBuilder> defineUserFunction) {

		return findByUsername(username)
				.orElseGet(() -> {

					var builder = UserAccount.builder()
							.userName(username)
							.password(UUID.randomUUID().toString())
							.role(ANONYMOUS);

					var user = defineUserFunction.apply(builder).build();

					return create(user, user.usesMfa());
				});
	}

	@SneakyThrows
	public String generateQrCodeImageUri(@NonNull UserAccount user) {

		Assert.isTrue(isItCurrentUser(user.getId()), "Only the authenticated user can get his own Secret as QR code!");
		Assert.isTrue(user.usesMfa(), "Can't supply the MFA QR for a user without enabled MFA!");

		QrData data = qrDataFactory.newBuilder()
				.label(APP_NAME + ":" + user.getUserName())
				.secret(user.getMfaSecret())
				.issuer(APP_NAME)
				.build();

		Supplier<String> errorMessage = () -> "Can't generate the QR code image!";

		// Generate the QR code image data as a base64 string which
		// can be used in an <img> tag:
		return Try.of(() -> qrGenerator.generate(data))
				.map(it -> getDataUriForImage(it, qrGenerator.getImageMimeType()))
				.onFailure(it -> alertService.createAlertMessage(errorMessage.get(), it.getMessage()))
				.getOrElseThrow(it -> new QrGenerationException(errorMessage.get(), it.getCause())); // sets a better message
	}

	/**
	 * If the parameter {@code useMfa} is {@code True}, a new secret will created for the {@link UserAccount} with
	 * {@link UserAccount#createMfaSecret(SecretGenerator)}. If the parameter is {@code False}, the secret will deleted
	 * for the {@link UserAccount}. However, a change is only made if {@code useMfa} is different from the result of
	 * {@link UserAccount#isUseMfa()}.
	 * <p />
	 * This method will only run successful if the current user an admin or the same user as in parameter {@code user}!
	 *
	 * @param user
	 * @param useMfa
	 * @return The modified and saved {@link UserAccount}.
	 */
	@NonNull
	public UserAccount updateUseMfa(@NonNull UserAccount user, boolean useMfa) {

		if (user.usesMfa() == useMfa) {
			return user;
		}

		checkLegitimacyOfCall(user.getId());

		return users.save(useMfa
				? user.createMfaSecret(secretGenerator::generate)
				: user.deleteMfaSecret());
	}

	public Optional<UserAccount> resetMfaSecret(UserAccountIdentifier id) {
		return users.findUserById(id).map(this::resetMfaSecret);
	}

	public UserAccount resetMfaSecret(UserAccount user) {

		checkLegitimacyOfCall(user.getId());

		Assert.isTrue(user.usesMfa(), "Only for a user with activated MFA a reset of the secret is possible!");

		return users.save(user.createMfaSecret(secretGenerator::generate));
	}

	public boolean finishEnrollment(@NotNull UserAccount user, String otp) {

		Assert.isTrue(isItCurrentUser(user.getId()), "Only the authenticated user can verify an OTP for him self!");
		Assert.isTrue(user.usesMfa(), "Can't verify an OTP for a user without enabled MFA!");

		var validCode = codeVerifier.isValidCode(user.getMfaSecret(), otp);

		if (validCode) {
			users.save(user.markAsEnrolled());
		}

		return validCode;
	}

	public boolean verifyOtp(@NotNull UserAccount user, String otp) {

		if (user.usesMfa()) {
			return codeVerifier.isValidCode(user.getMfaSecret(), otp);
		}
		return false;
	}

	private void checkLegitimacyOfCall(UserAccountIdentifier id) {
		Assert.isTrue(authManager.isAdmin() || isItCurrentUser(id), "Only an admin user can change other users!");
	}

	private UserAccount loadUser(UserAccountIdentifier userId) {

		return users.findUserById(userId)
				.orElseThrow(() -> {
					var error = "User not found: " + userId.toString();
					log.error(error);
					throw new RuntimeException(error);
				});
	}

	private boolean isRemoveLastAdmin(UserAccount userAccount, UserRole newRole) {
		return newRole != UserRole.ADMIN
				&& userAccount.getRole() == UserRole.ADMIN
				&& users.countUsersByRole(UserRole.ADMIN) == 1;
	}
}
