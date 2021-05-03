package de.healthIMIS.iris.client.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;

@Configuration
public class JacksonConfig {

	@Bean
	Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {

		return builder -> {
			builder.postConfigurer(it -> {
				// for SORMAS timestamps
				it.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
			});
		};
	}
}
