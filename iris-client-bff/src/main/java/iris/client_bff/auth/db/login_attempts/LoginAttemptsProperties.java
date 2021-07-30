package iris.client_bff.auth.db.login_attempts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Jens Kutzsche
 */
@ConfigurationProperties(prefix = "security.login.attempts")
@ConstructorBinding
@Data
@AllArgsConstructor
public class LoginAttemptsProperties {

	private int firstWarningThreshold;
	private int warningThresholdMultiplier;
	private Duration firstWaitingTime;
	private int waitingTimeMultiplier;
	private Duration ignoreOldAttemptsAfter;
}
