package iris.client_bff.config;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Jens Kutzsche
 */
@Configuration
class ValidationConfig {

	@Bean
	LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {

		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean() {
			@Override
			protected void postProcessConfiguration(javax.validation.Configuration<?> configuration) {
				super.postProcessConfiguration(configuration);

				configuration.addValueExtractor(new DataSizeValueExtractor());
			}
		};
		bean.setValidationMessageSource(messageSource);

		return bean;
	}

	static class DataSizeValueExtractor implements ValueExtractor<@ExtractedValue(type = Long.class) DataSize> {

		@Override
		public void extractValues(DataSize originalValue, ValueReceiver receiver) {
			receiver.value("DataSize", originalValue.toBytes());
		}
	}
}
