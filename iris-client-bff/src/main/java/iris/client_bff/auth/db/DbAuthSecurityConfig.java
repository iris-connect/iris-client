package iris.client_bff.auth.db;

import static iris.client_bff.config.DataSubmissionConfig.*;

import io.vavr.control.Try;
import iris.client_bff.auth.db.jwt.JWTService;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserRole;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@EnableWebSecurity
@RequiredArgsConstructor
public class DbAuthSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String USER_NOT_FOUND = "User: %s, not found";

	private static final String[] SWAGGER_WHITELIST = {
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/api-docs/**"
	};

	private final Environment env;

	private final UserService userService;

	private final JWTService jwtService;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final AccessDeniedHandler accessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		if (StringUtils.equalsAny("dev", env.getActiveProfiles())) {
			http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
			http.headers().frameOptions().sameOrigin();
		}

		http.cors().and()
				.csrf().disable()
				.oauth2ResourceServer(it -> it
						.bearerTokenResolver(bearerTokenResolver())
						.authenticationEntryPoint(authenticationEntryPoint)
						.accessDeniedHandler(accessDeniedHandler)
						.jwt(jwt -> jwt
								.decoder(jwtDecoder())
								.jwtAuthenticationConverter(authenticationConverter())))
				.authorizeRequests(it -> it
						.antMatchers(SWAGGER_WHITELIST).permitAll()
						.requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
						.requestMatchers(EndpointRequest.toAnyEndpoint()).hasAuthority(UserRole.ADMIN.name())
						.antMatchers(HttpMethod.POST, DATA_SUBMISSION_ENDPOINT).permitAll()
						.antMatchers(HttpMethod.POST, DATA_SUBMISSION_ENDPOINT_WITH_SLASH).permitAll()
						.antMatchers("/login", "/error").permitAll()
						.anyRequest().authenticated())
				.logout(it -> it
						.logoutUrl("/user/logout")
						.addLogoutHandler(logoutHandler())
						.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
						.permitAll())
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() {

		return username -> {

			var user = findUser(username);

			// By convention we expect that there exists only one authority and it represents the role
			var authorities = AuthorityUtils.createAuthorityList(user.getRole().name());

			return new User(user.getUserName(), user.getPassword(), user.isEnabled(), true, true,
					!user.isLocked(), authorities);
		};
	}

	@Bean
	BearerTokenResolver bearerTokenResolver() {
		return new DefaultBearerTokenResolver();
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return jwtService.createJwtDecoder();
	}

	Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {

		return jwt -> new JwtAuthenticationToken(jwt, findUser(jwt.getSubject()));
	}

	LogoutHandler logoutHandler() {

		return (req, __, ___) -> Try.success(req)
				.map(bearerTokenResolver()::resolve)
				.map(jwtDecoder()::decode)
				.map(Jwt::getSubject)
				.onSuccess(jwtService::invalidateTokensOfUser);
	}

	private UserAccount findUser(String username) {

		return userService.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
	}
}
