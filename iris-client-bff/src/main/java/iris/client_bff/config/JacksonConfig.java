package iris.client_bff.config;

import static org.apache.commons.lang3.StringUtils.*;

import iris.client_bff.core.IdWithUuid;
import iris.client_bff.core.converters.PrimitivesToIdWithUuidConverter;
import iris.client_bff.core.validation.AttackDetector;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import javax.annotation.Nullable;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.TypeDescriptor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Configuration
public class JacksonConfig {

	public static final String ATTACK_DETECTOR = "AttackDetector";
	private static final String ID_WITH_UUID_CONVERTER = "IdWithUuidConverter";

	@Bean
	Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(AttackDetector attackDetector,
			PrimitivesToIdWithUuidConverter converter) {

		return builder -> builder.postConfigurer(it -> {
			// for SORMAS timestamps
			it.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

			it.setInjectableValues(new InjectableValues.Std().addValue(ATTACK_DETECTOR, attackDetector)
					.addValue(ID_WITH_UUID_CONVERTER, converter));
		});
	}

	@Bean
	Module module(MessageSourceAccessor messages) {

		var module = new SimpleModule();

		module.setMixInAnnotation(IdWithUuid.class, IdWithUuidMixin.class);
		module.addDeserializer(String.class, new IrisStringDeserializer());
		module.addSerializer(new MessageSourceResolvableSerializer(messages));

		return module;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = IdWithUuidDeserializer.class) // Must be @JsonDeserialize to use ContextualDeserializer
																													// successful.
	interface IdWithUuidMixin {}

	/**
	 * Deserialize strings of UUIDs to subtypes of {@link IdWithUuid} and uses {@link PrimitivesToIdWithUuidConverter} for
	 * this.
	 *
	 * @author Jens Kutzsche
	 */
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true)
	static class IdWithUuidDeserializer<T extends IdWithUuid> extends JsonDeserializer<T>
			implements ContextualDeserializer {

		private final PrimitivesToIdWithUuidConverter converter;
		private final Class<T> clazz;

		@Override
		public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

			return (T) converter.convert(jp.getText(), TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(clazz));
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, @Nullable BeanProperty property)
				throws JsonMappingException {

			var type = property == null
					? ctxt.getContextualType()
					: property.getType();

			var targetType = (Class<? extends IdWithUuid>) type.getRawClass();

			var converter = (PrimitivesToIdWithUuidConverter) ctxt
					.findInjectableValue(ID_WITH_UUID_CONVERTER, null, null);

			return new IdWithUuidDeserializer(converter, targetType);
		}
	}

	/**
	 * Deserialize blank texts to {@code Null} and trims other texts.
	 *
	 * @author Jens Kutzsche
	 */
	class IrisStringDeserializer extends JsonDeserializer<String> {

		@Override
		public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

			return isBlank(jp.getText())
					? null
					: StringDeserializer.instance.deserialize(jp, ctxt).trim();
		}
	}

	/**
	 * A Jackson serializer triggering message resolution via a {@link MessageSourceAccessor} for
	 * {@link MessageSourceResolvable} instances about to be serialized.
	 *
	 * @author Oliver Drotbohm (from Quarano project)
	 * @author Jens Kutzsche
	 */
	class MessageSourceResolvableSerializer extends StdSerializer<MessageSourceResolvable> {

		private static final long serialVersionUID = 1476624713270693120L;

		private final MessageSourceAccessor messages;

		private MessageSourceResolvableSerializer(MessageSourceAccessor messages) {
			super(MessageSourceResolvable.class);

			this.messages = messages;
		}

		@Override
		public void serialize(MessageSourceResolvable value, JsonGenerator gen, SerializerProvider provider)
				throws IOException {
			gen.writeString(messages.getMessage(value));
		}
	}
}
