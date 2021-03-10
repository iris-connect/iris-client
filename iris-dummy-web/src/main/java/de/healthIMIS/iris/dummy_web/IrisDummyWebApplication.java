package de.healthIMIS.iris.dummy_web;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IrisDummyWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrisDummyWebApplication.class, args); // <2>
	}

	@Bean
	RestTemplate getRestTemplate(RestTemplateBuilder builder) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		var sslContext = new SSLContextBuilder().loadTrustMaterial((chain, authType) -> true) // trust all server certificates
			.build();

		var socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		var httpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();

		var restTemplate = builder.build();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

		return restTemplate;
	}

	@Bean
	Traverson getTraverson(IrisProperties properties, RestTemplate rest) {

		var uriStr = String.format("https://%s:%s/", properties.getServerAddress().getHostAddress(), properties.getServerPort());
		var traverson = new Traverson(URI.create(uriStr), MediaTypes.HAL_JSON);
		traverson.setRestOperations(rest);

		return traverson;
	}
}
