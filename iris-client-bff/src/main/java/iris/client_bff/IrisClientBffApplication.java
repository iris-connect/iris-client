package iris.client_bff;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IrisClientBffApplication {

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

		var application = new SpringApplication(IrisClientBffApplication.class);
		application.setBanner(banner);
		application.setBannerMode(Mode.LOG);
		application.run(args);
	}
}
