package iris.client_bff.config;

import static iris.client_bff.config.DataSubmissionConfig.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig {

	@Bean
	public WebMvcConfigurer configureCorsForJsonRPC() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping(DATA_SUBMISSION_ENDPOINT)
						.allowedOrigins("*")
						.allowedMethods("OPTIONS", "POST")
						.allowedHeaders("*");
				registry
						.addMapping(DATA_SUBMISSION_ENDPOINT_WITH_SLASH)
						.allowedOrigins("*")
						.allowedMethods("OPTIONS", "POST")
						.allowedHeaders("*");
			}
		};
	}

	@Bean
	@Profile({ "local", "dev" })
	public WebMvcConfigurer configureCorsForLocalDev() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/**")
						.allowedOriginPatterns("*")
						.allowedMethods("*")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}
}
