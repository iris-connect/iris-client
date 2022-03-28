package iris.client_bff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Jens Kutzsche
 */
@Configuration
@EnableScheduling
@Profile({ "!test", "!inttest" })
public class SchedulingConfig {}
