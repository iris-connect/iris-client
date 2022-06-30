package iris.client_bff;

import static org.springframework.security.core.authority.AuthorityUtils.*;

import iris.client_bff.WithMockAdmin.WithIrisUserMockSecurityContextFactory;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

//@WithMockUser(value = "admin", authorities = "ADMIN")
@WithSecurityContext(factory = WithIrisUserMockSecurityContextFactory.class)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithMockAdmin {

	@RequiredArgsConstructor
	static class WithIrisUserMockSecurityContextFactory implements WithSecurityContextFactory<WithMockAdmin> {

		private final UserService userService;

		@Override
		public SecurityContext createSecurityContext(WithMockAdmin annotation) {

			var user = userService.findByUsername("admin").get();

			var authorities = createAuthorityList(user.getRole().toString());
			var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

			var context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication);

			return context;
		}
	}
}
