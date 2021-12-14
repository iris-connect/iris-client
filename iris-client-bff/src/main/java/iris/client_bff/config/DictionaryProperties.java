package iris.client_bff.config;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/**
 * @author Jens Kutzsche
 */
@ConfigurationProperties("dictionary")
@ConstructorBinding
@Value
@Validated
public class DictionaryProperties {

	int size;

	DictionaryProperties(@DefaultValue("20000") int size) {

		this.size = size;
	}
}
