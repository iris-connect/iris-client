package iris.client_bff.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "iris.backend-service")
@Getter
@AllArgsConstructor
public class BackendServiceProperties {

	private String endpoint;
}
