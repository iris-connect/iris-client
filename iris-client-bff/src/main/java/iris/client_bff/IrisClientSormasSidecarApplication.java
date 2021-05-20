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
package iris.client_bff;

import iris.client_bff.events.eps.DataSubmissionRPCImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;

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
	application.setBannerMode(Mode.LOG);
	application.run(args);
  }

  @Bean
  @Profile({ "local", "dev" })
  public WebMvcConfigurer corsConfigurer() {
	return new WebMvcConfigurer() {
	  @Override
	  public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("*")
			.allowedHeaders("*");
	  }
	};
  }

  @Bean
  public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
	return new AutoJsonRpcServiceImplExporter();
  }

  @Autowired
  public DataSubmissionRPCImpl dataSubmissionRPCImpl;
}
