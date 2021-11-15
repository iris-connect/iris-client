package iris.client_bff.config;

import iris.client_bff.core.utils.ValidatedProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hd")
@Getter
@Setter
public class HealthDepartmentProperties extends ValidatedProperties {

	HealthDepartmentProperties(Validator validator) {
		super(HealthDepartmentProperties.class, validator);
	}

	@NotBlank(message = "{missing.property.hd.abbreviation}") //
	@Pattern(regexp = "^[0123456789ABCDEFabcdef]{3}$", message = "{missing.property.hd.abbreviation}")
	private String abbreviation;
}
