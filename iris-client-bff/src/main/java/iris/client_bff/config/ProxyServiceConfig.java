package iris.client_bff.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
@ConfigurationProperties("proxy-service")
@Validated
public class ProxyServiceConfig {
	private String epsName;
	private String targetProxy;
	private String targetSubdomain;

	@NotBlank
	private String publicProxyDomain;
}
