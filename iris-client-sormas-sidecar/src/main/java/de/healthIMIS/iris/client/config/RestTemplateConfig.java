package de.healthIMIS.iris.client.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Value("${http.client.ssl.key-store}")
	private Resource keyStore;
	@Value("${http.client.ssl.key-store-password}")
	private String keyStorePassword;
	@Value("${http.client.ssl.key-password}")
	private String keyPassword;
	@Value("${http.client.ssl.trust-store}")
	private Resource trustStore;
	@Value("${http.client.ssl.trust-store-password}")
	private String trustStorePassword;
	@Value("${http.client.proxy.host:#{null}}")
	private String httpProxyHost;
	@Value("${http.client.proxy.port:#{null}}")
	private Integer httpProxyPort;
	@Value("${http.client.proxy.scheme:http}")
	private String httpProxyScheme;

	@Bean(name = "iris-rest")
	RestTemplate getIrisRestTemplate(RestTemplateBuilder builder) throws KeyManagementException,
			UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

		var sslContext = new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray(), (chain, authType) -> false)
				.loadKeyMaterial(keyStore.getURL(), keyStorePassword.toCharArray(), keyPassword.toCharArray()).build();

		var socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		var httpClientBuilder = HttpClientBuilder.create().setSSLSocketFactory(socketFactory);

		if (httpProxyHost != null && httpProxyPort != null) {
			httpClientBuilder.setProxy(new HttpHost(httpProxyHost, httpProxyPort, httpProxyScheme));
		}

		var restTemplate = builder.build();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build()));

		// if (log.isDebugEnabled()) {
		// restTemplate.setInterceptors(List.of(new RequestResponseLoggingInterceptor()));
		// }

		return restTemplate;
	}

	@Bean(name = "sormas-rest")
	RestTemplate getSormasRestTemplate(RestTemplateBuilder builder) {

		var restTemplate = builder.build();

		// if (log.isDebugEnabled()) {
		// restTemplate.setInterceptors(List.of(new RequestResponseLoggingInterceptor()));
		// }

		return restTemplate;
	}
}
