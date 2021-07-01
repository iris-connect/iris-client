package iris.client_bff.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@RequiredArgsConstructor
@ConfigurationProperties("ext-app.dw")
public class DwConfig {
	private String baseurl;
	private String suburlNewcase;
}
