package iris.client_bff.config;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
public class BackendServiceProperties {

	@Value("${iris.backend-service.endpoint:#{null}}")
	private String endpoint;

	@Value("${iris.location-service.endpoint:#{null}}")
	private String endpointOld;

	@NotBlank
	public String getEndpoint() {
		return StringUtils.isNotBlank(endpoint) ? endpoint : endpointOld;
	}
}
