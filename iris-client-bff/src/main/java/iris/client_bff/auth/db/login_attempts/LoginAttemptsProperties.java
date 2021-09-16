package iris.client_bff.auth.db.login_attempts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

/**
 * @author Jens Kutzsche
 */
@ConfigurationProperties(prefix = "security.login.attempts")
@ConstructorBinding
@Data
@AllArgsConstructor
@Validated
public class LoginAttemptsProperties {

	@Positive
	private int firstWarningThreshold;
	@Positive
	private int warningThresholdMultiplier;
	@DurationMin(seconds = 1)
	private Duration firstWaitingTime;
	@Positive
	private int waitingTimeMultiplier;
	@DurationMin(hours = 1)
	private Duration ignoreOldAttemptsAfter;
}
