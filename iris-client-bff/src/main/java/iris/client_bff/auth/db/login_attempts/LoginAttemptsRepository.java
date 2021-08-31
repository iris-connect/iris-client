package iris.client_bff.auth.db.login_attempts;

import java.time.Instant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jens Kutzsche
 */
public interface LoginAttemptsRepository extends CrudRepository<LoginAttempts, String> {

	@Transactional
	void deleteBylastModifiedBefore(Instant time);
}
