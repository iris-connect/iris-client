package iris.client_bff.auth.db.login_attempts;

import java.time.Instant;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jens Kutzsche
 */
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
interface LoginAttemptsRepository extends CrudRepository<LoginAttempts, String> {

	@Transactional
	void deleteBylastModifiedBefore(Instant time);
}
