package iris.client_bff.auth.db;

import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
@Slf4j
public class OtpAuthenticationProvider implements AuthenticationProvider {

	private static final String USER_NOT_FOUND = "User: %s, not found";

	private final UserService userService;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {

		var username = auth.getName();
		var otp = Objects.toString(auth.getCredentials());
		var user = userService.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));

		var valid = user.isMfaSecretEnrolled()
				? userService.verifyOtp(user, otp)
				: userService.finishEnrollment(user, otp);

		if (!valid) {
			throw new BadCredentialsException("Invalid verification code");
		}

		var resultAuth = new OtpAuthenticationToken(user, null);
		resultAuth.setDetails(auth.getDetails());
		log.debug("Authenticated user");

		return resultAuth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OtpAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
