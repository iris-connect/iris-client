package iris.client_bff.auth.db;

import static iris.client_bff.config.DataSubmissionConfig.*;

import iris.client_bff.auth.db.jwt.JWTSigner;
import iris.client_bff.auth.db.jwt.JWTVerifier;
import iris.client_bff.auth.db.login_attempts.LoginAttemptsService;
import iris.client_bff.users.UserRole;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@EnableWebSecurity
@RequiredArgsConstructor
public class DbAuthSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] SWAGGER_WHITELIST = {

			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/api-docs/**"
	};

	private final Environment env;

	private final CustomLogoutHandler logoutHandler;

	private final JWTVerifier jwtVerifier;

	private final JWTSigner jwtSigner;

	private final LoginAttemptsService loginAttempts;

	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	private final UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		if (StringUtils.equalsAny("dev", env.getActiveProfiles())) {
			http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
			http.headers().frameOptions().sameOrigin();
		}

		http.cors().and()
				.csrf().disable()
				.authorizeRequests(it -> it
						.antMatchers("/error").permitAll()
						.antMatchers(SWAGGER_WHITELIST).permitAll()
						.antMatchers(HttpMethod.POST, DATA_SUBMISSION_ENDPOINT).permitAll()
						.antMatchers(HttpMethod.POST, DATA_SUBMISSION_ENDPOINT_WITH_SLASH).permitAll()
						.requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
						.requestMatchers(EndpointRequest.toAnyEndpoint()).hasAuthority(UserRole.ADMIN.name())
						.anyRequest().authenticated())
				.logout(it -> it
						.logoutUrl("/user/logout")
						.addLogoutHandler(logoutHandler)
						.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
						.permitAll())
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtSigner, loginAttempts))
				.addFilterAfter(new JWTAuthorizationFilter(jwtVerifier, userService), JWTAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
}
