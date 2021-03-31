/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.client;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.http.HttpMethod.*;

import de.healthIMIS.iris.client.core.IrisClientProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "irisDateTimeProvider")
@ConfigurationPropertiesScan
public class IrisClientSormasSidecarApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrisClientSormasSidecarApplication.class, args);
	}

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

	@Bean
	@ConditionalOnProperty(name = "iris.client.key-store", matchIfMissing = true)
	KeyStore getKeyStore(IrisClientProperties properties)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

		char[] password = properties.getKeyStorePassword().toCharArray();
		ks.load(null, password);

		return ks;
	}

	@PostConstruct
	public void initBouncyCastle() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Bean
	Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			builder.postConfigurer(it -> {
				// for SORMAS timestamps
				it.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
			});
		};
	}

	@Bean(name = "iris-rest")
	RestTemplate getIrisRestTemplate(RestTemplateBuilder builder) throws KeyManagementException,
			UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

		var sslContext = new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray(), (chain, authType) -> false)
				.loadKeyMaterial(keyStore.getURL(), keyStorePassword.toCharArray(), keyPassword.toCharArray()).build();

		var socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		var httpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();

		var restTemplate = builder.build();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

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

	@Bean(name = "search-rest")
	RestTemplate getSearchRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	FlywayMigrationStrategy getFlywayMigrationStrategy(Environment env) {

		return flyway -> {

			var profiles_clean = List.of("psql_compose_db");
			if (Arrays.stream(env.getActiveProfiles()).anyMatch(profiles_clean::contains)) {
				flyway.clean();
			}

			if (log.isDebugEnabled()) {

				var results = flyway.validateWithResult();

				results.invalidMigrations.forEach(it -> {

					var errorDetails = it.errorDetails;

					log.debug("ValidateOutput: " + it.description + errorDetails != null
							? " | ErrorCode: " + errorDetails.errorCode + " | ErrorMessage: " + errorDetails.errorMessage
							: "");
				});
			}

			flyway.migrate();
		};
	}

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		return modelMapper;
	}

	// ToDo: Let us check if a) remove it or b) perform a real UTF-8 check against the payload
	static final class UpdateRequestNeedCharacterEncodingFilter extends OrderedCharacterEncodingFilter {

		static final EnumSet<HttpMethod> updateMethods = EnumSet.of(PUT, POST, PATCH);

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {

			if (isUpdateMethod(request) && isUtf8Missing(request)) {

				response.sendError(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Character encoding must be UTF-8!");

				return;
			}

			super.doFilterInternal(request, response, filterChain);
		}

		private boolean isUpdateMethod(HttpServletRequest request) {
			return updateMethods.contains(resolve(request.getMethod()));
		}

		private boolean isUtf8Missing(HttpServletRequest request) {
			return !equalsIgnoreCase(request.getCharacterEncoding(), "UTF-8");
		}
	}

	@Configuration
	@EnableScheduling
	static class SchedulingProperties {}
}
