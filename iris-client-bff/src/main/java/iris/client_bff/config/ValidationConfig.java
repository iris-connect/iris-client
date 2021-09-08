package iris.client_bff.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Jens Kutzsche
 */
@Configuration
public class ValidationConfig {

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {

		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);

		return bean;
	}
}
