package iris.client_bff.config;

import iris.client_bff.core.validation.AttackDetector;

import java.io.IOException;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@Configuration
public class JacksonConfig {

	public static final String ATTACK_DETECTOR = "AttackDetector";

	@Bean
	Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(AttackDetector attackDetector) {

		return builder -> {
			builder.postConfigurer(it -> {
				// for SORMAS timestamps
				it.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

				it.setInjectableValues(new InjectableValues.Std().addValue(ATTACK_DETECTOR, attackDetector));
			});
		};
	}

	@Bean
	Module module(MessageSourceAccessor messages) {

		var module = new SimpleModule();
		module.addSerializer(new StdSerializer<>(MessageSourceResolvable.class) {

			private static final long serialVersionUID = 5598637634112288197L;

			@Override
			public void serialize(MessageSourceResolvable value, JsonGenerator gen, SerializerProvider provider)
					throws IOException {
				gen.writeString(messages.getMessage(value));
			}
		});

		return module;
	}
}
