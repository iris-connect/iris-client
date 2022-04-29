package iris.client_bff.users;

import static java.time.Instant.*;

import iris.client_bff.users.entities.UserAccount;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import javax.transaction.Transactional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class collects all old events and deletes this.
 *
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
class UserDeleteJob {

	private final @NonNull UserAccountsRepository users;
	private final @NonNull UserDeletionProperties properties;

	@Transactional
	@Scheduled(cron = "${iris.client.user.delete-cron:-}")
	void deleteUsers() {

		users.findAllDeleted().forEach(it -> {

			log.debug("Try to delete marked user with ID = {}!", it.getId());

			try {

				users.delete(it);
				log.info("User with ID = {} deleted!", it.getId());

			} catch (DataIntegrityViolationException e) {

				if (shouldBeLoggedAsToOldSoftDelete(it)) {
					log.warn("User with ID = {} could not be deleted!", it.getId());
				}
			}
		});
	}

	private boolean shouldBeLoggedAsToOldSoftDelete(UserAccount user) {
		return now().minus(properties.getDeleteErrorWarningAfter()).isAfter(user.getDeletedAt());
	}

	@ConfigurationProperties("iris.client.user")
	@ConstructorBinding
	@Getter
	@RequiredArgsConstructor
	static class UserDeletionProperties {

		/**
		 * Defines the {@link Duration} after that errors during deletion () will be logged.
		 */
		private final Duration deleteErrorWarningAfter;
	}
}
