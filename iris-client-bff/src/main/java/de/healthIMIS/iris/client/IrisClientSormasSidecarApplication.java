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

import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "irisDateTimeProvider")
@EnableScheduling
@ConfigurationPropertiesScan
public class IrisClientSormasSidecarApplication {

	public static void main(String[] args) throws Exception {

		var properties = PropertiesLoaderUtils.loadAllProperties("git.properties");
		var banner = new ResourceBanner(new ClassPathResource("iris-banner.txt")) {
			@Override
			protected String getApplicationVersion(Class<?> sourceClass) {
				return properties.getProperty("git.build.version", "-") + " ("
						+ properties.getProperty("git.commit.id.abbrev", "-")
						+ ")";
			}
		};

		var application = new SpringApplication(IrisClientSormasSidecarApplication.class);
		application.setBanner(banner);
		application.run(args);
	}
}
