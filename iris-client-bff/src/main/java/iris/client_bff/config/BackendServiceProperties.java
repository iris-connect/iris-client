package iris.client_bff.config;

import iris.client_bff.core.utils.ValidatedProperties;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Validator;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BackendServiceProperties extends ValidatedProperties {

	BackendServiceProperties(Validator validator) {
		super(BackendServiceProperties.class, validator);
	}

	@Value("${iris.backend-service.endpoint:#{null}}")
	private String endpoint;

	@Value("${iris.location-service.endpoint:#{null}}")
	private String endpointOld;

	@NotBlank(message = "{missing.property.iris.backend-service.endpoint}")
	public String getEndpoint() {
		return StringUtils.isNotBlank(endpoint) ? endpoint : endpointOld;
	}
}
