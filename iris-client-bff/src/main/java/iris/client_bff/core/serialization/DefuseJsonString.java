package iris.client_bff.core.serialization;

import iris.client_bff.config.JacksonConfig;
import iris.client_bff.core.serialization.DefuseJsonString.DefuseJsonStringDeserializer;
import iris.client_bff.core.validation.AttackDetector;
import iris.client_bff.core.validation.AttackDetector.MessageDataPayload;
import iris.client_bff.core.validation.AttackDetector.Password;
import iris.client_bff.core.validation.AttackDetector.Phone;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Payload;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@JacksonAnnotationsInside
@JsonDeserialize(using = DefuseJsonStringDeserializer.class)
public @interface DefuseJsonString {

	public static final String INVALID_INPUT_STRING = "INVALID";

	boolean obfuscateLogging()

	default true;

	Class<? extends Payload>[] payload() default {};

	int maxLength();

	static class DefuseJsonStringDeserializer extends StringDeserializer implements ContextualDeserializer {

		private static final long serialVersionUID = 6145556502252440787L;

		private String path;
		private boolean obfuscateLogging;
		private Class<? extends Payload> type;
		private int maxLength;

		@Override
		public String deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {

			var attackDetector = (AttackDetector) ctxt.findInjectableValue(JacksonConfig.ATTACK_DETECTOR, null, null);

			var value = super.deserialize(parser, ctxt);

			boolean attackDetected = false;
			if (type == Phone.class) {
				attackDetected = attackDetector.isPossibleAttackForPhone(value, path, obfuscateLogging);
			} else if (type == Password.class) {
				attackDetected = attackDetector.isPossibleAttackForPassword(value, path);
			} else if (type == MessageDataPayload.class) {

				if (value != null
						&& (attackDetector.isPossibleAttackForMessageDataPayload(value, path, obfuscateLogging)
								|| value.length() > maxLength)) {

					return "";
				}

			} else {
				attackDetected = attackDetector.isPossibleAttack(value, path, obfuscateLogging);
			}

			if (attackDetected) {
				return INVALID_INPUT_STRING;
			}
			return maxLength < 0 ? value : StringUtils.truncate(value, maxLength);
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
				throws JsonMappingException {

			var annotation = property.getAnnotation(DefuseJsonString.class);

			path = String.format("%s#%s", property.getMember().getRawType().getName(), property.getName());
			obfuscateLogging = annotation.obfuscateLogging();
			maxLength = annotation.maxLength();

			var payloads = annotation.payload();

			Assert.isTrue(payloads.length <= 1, "Only one type can be defined!");

			if (payloads.length == 1) {
				this.type = payloads[0];
			}

			return this;
		}
	}
}
