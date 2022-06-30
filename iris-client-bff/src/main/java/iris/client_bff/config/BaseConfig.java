package iris.client_bff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Jens Kutzsche
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "irisDateTimeProvider")
public class BaseConfig {

}
