package iris.client_bff.users;

import dev.samstevens.totp.secret.SecretGenerator;
import iris.client_bff.core.model.Aggregate;
import iris.client_bff.core.model.IdWithUuid;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "user_accounts")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccount extends Aggregate<UserAccount, UserAccountIdentifier> {

	{
		id = UserAccountIdentifier.random();
	}

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String password;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	private boolean locked;
	private Instant deletedAt;

	private String mfaSecret;
	@Setter(AccessLevel.PRIVATE)
	private boolean mfaSecretEnrolled;

	public boolean isEnabled() {
		return getDeletedAt() == null;
	}

	public boolean isAdmin() {
		return getRole() == UserRole.ADMIN;
	}

	private UserAccount setMfaSecret(String secret) {

		mfaSecret = secret;
		setMfaSecretEnrolled(false);

		return this;
	}

	public UserAccount markDeleted() {

		var name = getUserName();

		setDeletedAt(Instant.now());
		setUserName(getId().toString());

		registerEvent(UserMarkedDeleted.of(id, name));

		return this;
	}

	public UserAccount markLoginIncompatiblyUpdated() {

		registerEvent(UserLoginIncompatiblyUpdated.of(id, getUserName()));

		return this;
	}

	/**
	 * Creates a secret with the given {@link SecretGenerator} and sets it to {@link #mfaSecret}.
	 *
	 * @param secretGenerator
	 * @return This {@link UserAccount}
	 */
	UserAccount createMfaSecret(Supplier<String> secretGenerator) {

		String secret = secretGenerator.get();

		if (StringUtils.isBlank(secret)) {
			throw new RuntimeException("Generated secret for MFA is null or blank!");
		}

		return setMfaSecret(secret);
	}

	UserAccount deleteMfaSecret() {
		return setMfaSecret(null);
	}

	public boolean usesMfa() {
		return getMfaSecret() != null;
	}

	public UserAccount markAsEnrolled() {
		return setMfaSecretEnrolled(true);
	}

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // for JPA
	public static class UserAccountIdentifier extends IdWithUuid {

		private static final long serialVersionUID = 691185456738401495L;

		final UUID userId;

		public static UserAccountIdentifier random() {
			return of(UUID.randomUUID());
		}

		@Override
		protected UUID getBasicId() {
			return userId;
		}
	}

	@Value(staticConstructor = "of")
	public static class UserMarkedDeleted {

		UserAccountIdentifier userId;
		String userName;
	}

	@Value(staticConstructor = "of")
	public static class UserLoginIncompatiblyUpdated {

		UserAccountIdentifier userId;
		String userName;
	}
}
