package iris.client_bff.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@RequiredArgsConstructor
@ConfigurationProperties("proxy-service")
public class ProxyServiceConfig {
	private String epsName;
	private String targetProxy;
	private String targetSubdomain;
}
