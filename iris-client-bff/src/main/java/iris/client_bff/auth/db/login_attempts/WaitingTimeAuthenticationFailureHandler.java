package iris.client_bff.auth.db.login_attempts;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author Jens Kutzsche
 */
@Slf4j
public final class WaitingTimeAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		log.debug("Sending 401 Unauthorized error");

		var msg = HttpStatus.UNAUTHORIZED.getReasonPhrase();
		if (exception instanceof AuthenticationException
				&& exception.getMessage().contains("User blocked")) {

			msg = exception.getMessage();
		}

		response.sendError(HttpStatus.UNAUTHORIZED.value(), msg);
	}
}
