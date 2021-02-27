/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.hd_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "irisDateTimeProvider")
@ConfigurationPropertiesScan
@Slf4j
public class IrisHdServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrisHdServerApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	FlywayMigrationStrategy getFlywayMigrationStrategy() {

		if (log.isDebugEnabled()) {
			return flyway -> {

				var results = flyway.validateWithResult();

				results.invalidMigrations.forEach(it -> {

					var errorDetails = it.errorDetails;

					log.debug(
						"ValidateOutput: " + it.description + errorDetails != null
							? " | ErrorCode: " + errorDetails.errorCode + " | ErrorMessage: " + errorDetails.errorMessage
							: "");
				});

				flyway.migrate();
			};
		} else {
			return null;
		}
	}

	@Configuration
	@EnableScheduling
	static class SchedulingProperties {
	}
}
