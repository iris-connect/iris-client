package iris.client_bff.auth.db.login_attempts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.util.Assert;

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

	@PostConstruct
	void checkValues() {
		Assert.isTrue(firstWarningThreshold > 0, "firstWarningThreshold must be > 0!");
		Assert.isTrue(warningThresholdMultiplier > 0, "warningThresholdMultiplier must be > 0!");
		Assert.isTrue(!firstWaitingTime.isNegative() && !firstWaitingTime.isZero(), "firstWaitingTime must be > 0!");
		Assert.isTrue(waitingTimeMultiplier > 0, "waitingTimeMultiplier must be > 0!");
		Assert.isTrue(!ignoreOldAttemptsAfter.isNegative() && !ignoreOldAttemptsAfter.isZero(),
				"ignoreOldAttemptsAfter must be > 0!");
	}
}
