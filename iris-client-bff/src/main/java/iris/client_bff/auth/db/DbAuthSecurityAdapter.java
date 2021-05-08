package iris.client_bff.auth.db;

import iris.client_bff.auth.db.jwt.JWTSigner;
import iris.client_bff.auth.db.jwt.JWTVerifier;
import iris.client_bff.users.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
public class DbAuthSecurityAdapter extends WebSecurityConfigurerAdapter {

	private static final String[] SWAGGER_WHITELIST = {

			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/api-docs/**"
	};

	private PasswordEncoder passwordEncoder;

	private JWTVerifier jwtVerifier;

	private JWTSigner jwtSigner;

	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable()
				.authorizeRequests()
				.antMatchers(SWAGGER_WHITELIST)
				.permitAll()
				.antMatchers(HttpMethod.POST, "/data-submission-rpc").permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilter(
						new JWTAuthenticationFilter(authenticationManager(), jwtSigner))
				.addFilter(
						new JWTAuthorizationFilter(authenticationManager(), jwtVerifier))
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

}
