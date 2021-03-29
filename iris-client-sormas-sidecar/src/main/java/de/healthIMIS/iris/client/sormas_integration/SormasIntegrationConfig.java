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
package de.healthIMIS.iris.client.sormas_integration;

import de.healthIMIS.sormas.client.api.CaseControllerApi;
import de.healthIMIS.sormas.client.api.ContactControllerApi;
import de.healthIMIS.sormas.client.api.EventControllerApi;
import de.healthIMIS.sormas.client.api.EventParticipantControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.SampleControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.invoker.ApiClient;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jens Kutzsche
 */
@Configuration
@ConditionalOnProperty("iris.sormas.user")
public class SormasIntegrationConfig {

	private final RestTemplate restTemplate;
	private final IrisSormasProperties properties;

	public SormasIntegrationConfig(@Qualifier("sormas-rest") RestTemplate restTemplate, IrisSormasProperties properties) {

		this.restTemplate = restTemplate;
		this.properties = properties;
	}

	@Bean
	public CaseControllerApi caseControllerApi() {

		return new CaseControllerApi(apiClient());
	}

	@Bean
	public PersonControllerApi personControllerApi() {

		return new PersonControllerApi(apiClient());
	}

	@Bean
	public TaskControllerApi taskControllerApi() {

		return new TaskControllerApi(apiClient());
	}

	@Bean
	public SampleControllerApi sampleControllerApi() {

		return new SampleControllerApi(apiClient());
	}

	@Bean
	public ContactControllerApi contactControllerApi() {

		return new ContactControllerApi(apiClient());
	}

	@Bean
	public EventControllerApi eventControllerApi() {

		return new EventControllerApi(apiClient());
	}

	@Bean
	public EventParticipantControllerApi participantControllerApi() {

		return new EventParticipantControllerApi(apiClient());
	}

	@Bean
	public ApiClient apiClient() {

		ApiClient apiClient = new IrisApiClient();
		// apiClient.setBasePath("https://sormas-docker-test.com/sormas-rest");
		apiClient.setBasePath(String.format("http://%s:%s/sormas-rest", properties.getServerAddress().getHostAddress(),
				properties.getServerPort()));
		apiClient.setUsername(properties.getUser().trim());
		apiClient.setPassword(properties.getPassword().trim());

		return apiClient;
	}

	class IrisApiClient extends ApiClient {

		@Override
		protected RestTemplate buildRestTemplate() {

			// var restTemplate = super.buildRestTemplate();

			var customRequestFactory = new HttpComponentsClientHttpRequestFactory();

			try {

				var builder = new SSLContextBuilder();
				builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
				var sslsf = new SSLConnectionSocketFactory(builder.build());
				var httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

				customRequestFactory.setHttpClient(httpClient);

			} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(customRequestFactory));

			return restTemplate;
		}
	}
}
