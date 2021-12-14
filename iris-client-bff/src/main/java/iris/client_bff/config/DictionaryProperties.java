package iris.client_bff.config;

import lombok.Value;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
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

	DictionaryProperties(@DefaultValue("20000") int size, String salt) {

		this.size = size;
		this.salt = salt;
	}

	int size;

	@NotBlank @Length(min = 32)
	String salt;
}
