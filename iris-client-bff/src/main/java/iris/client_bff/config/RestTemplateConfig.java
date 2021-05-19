package iris.client_bff.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean(name = "sormas-rest")
  RestTemplate getSormasRestTemplate(RestTemplateBuilder builder) {

	var restTemplate = builder.build();

	// if (log.isDebugEnabled()) {
	// restTemplate.setInterceptors(List.of(new RequestResponseLoggingInterceptor()));
	// }

	return restTemplate;
  }
}
