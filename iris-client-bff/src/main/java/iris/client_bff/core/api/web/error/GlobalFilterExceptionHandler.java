package iris.client_bff.core.api.web.error;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class GlobalFilterExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

	private final AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
	private final AccessDeniedHandler accessDeniedHandler = new BearerTokenAccessDeniedHandler();

	private final IrisErrorAttributes errorAttributes;

	/**
	 * handle authentication error
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		authenticationEntryPoint.commence(request, response, authException);

		response.sendError(response.getStatus());

		errorAttributes.resolveException(request, response, authenticationEntryPoint, authException);
	}

	/**
	 * handle access denied
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		accessDeniedHandler.handle(request, response, accessDeniedException);
		response.sendError(response.getStatus());

		errorAttributes.resolveException(request, response, accessDeniedHandler, accessDeniedException);
	}
}
