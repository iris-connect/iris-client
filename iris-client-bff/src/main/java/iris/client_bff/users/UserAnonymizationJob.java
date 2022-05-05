package iris.client_bff.users;

import static iris.client_bff.users.entities.UserRole.*;
import static java.lang.Boolean.*;
import static java.time.Instant.*;

import iris.client_bff.users.UserAnonymizationJob.Properties.AnonymizationBase;
import iris.client_bff.users.entities.UserAccount;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * This class collects all soft deleted users and anonymizes them if the requirements are met.
 *
 * @author Jens Kutzsche
 */
@Component
@Slf4j
class UserAnonymizationJob {

	static final String ANONYMOUS_TEXT = "#####";

	private final @NonNull UserAccountsRepository userRepo;
	private final @NonNull UserAnonymizationJob.Properties properties;

	private final Predicate<UserAccount> shouldAnonymizePredicate;

	public UserAnonymizationJob(@NonNull UserAccountsRepository userRepo,
			@NonNull UserAnonymizationJob.Properties properties,
			@NonNull List<UserAnonymizationReferenceChecker> referenceCheckers) {

		this.userRepo = userRepo;
		this.properties = properties;

		shouldAnonymizePredicate = determineShouldAnonymizePredicate(properties.getAnonymizationBase(), referenceCheckers);
	}

	private Predicate<UserAccount> determineShouldAnonymizePredicate(@NotNull AnonymizationBase anonymizationBase,
			@NonNull List<UserAnonymizationReferenceChecker> referenceCheckers) {

		Predicate<UserAccount> predicate = __ -> FALSE;

		if (anonymizationBase.isReferenceBased()) {

			Predicate<UserAccount> referenceBasedPredicate = referenceCheckers.stream()
					.reduce(__ -> TRUE, UserAnonymizationReferenceChecker::and);
			predicate = predicate.or(referenceBasedPredicate);
		}

		if (anonymizationBase.isTimeBased()) {

			Predicate<UserAccount> timeBasedPredicate = this::shouldBeAnonymized;
			predicate = predicate.or(timeBasedPredicate);
		}

		return predicate;
	}

	@Transactional
	@Scheduled(cron = "${iris.client.user.anonymization-cron:-}")
	public void processUsers() {
		userRepo.findAllDeleted().forEach(this::processUser);
	}

	private void processUser(UserAccount user) {

		if (shouldAnonymizePredicate.test(user)) {

			log.debug("Anonymize marked user with ID = {}!", user.getId());

			userRepo.save(anonymizeUser(user));

			log.info("User with ID = {} anonymized!", user.getId());

		} else if (shouldBeLoggedAsToOldSoftDelete(user)) {
			log.warn("User with ID = {} could not be anonymized, as references are still available!", user.getId());
		}
	}

	private UserAccount anonymizeUser(UserAccount user) {

		user.setFirstName(ANONYMOUS_TEXT);
		user.setLastName(ANONYMOUS_TEXT);
		user.setUserName(ANONYMOUS_TEXT);
		user.setPassword(ANONYMOUS_TEXT);
		user.setRole(DELETED);

		return user;
	}

	private boolean shouldBeAnonymized(UserAccount user) {
		return now().minus(properties.getAnonymizationAfter()).isAfter(user.getDeletedAt());
	}

	private boolean shouldBeLoggedAsToOldSoftDelete(UserAccount user) {
		return properties.getAnonymizationBase().isReferenceBased()
				&& now().minus(properties.getOldReferenceWarningAfter()).isAfter(user.getDeletedAt());
	}

	@ConfigurationProperties("iris.client.user")
	@ConstructorBinding
	@Value
	@Validated
	static class Properties {

		/**
		 * Defines the {@link Duration} after that users are deleted. This is only relevant if {@link anonymizationBase} is
		 * {@code TIME_BASED} or {@code REFERENCE_AND_TIME_BASED}.
		 */
		@NotNull
		@DurationMin(days = 0)
		Duration anonymizationAfter;

		/**
		 * Defines the {@link Duration} after which to warn via logs about old references that prevent anonymization. This
		 * is only relevant if {@link anonymizationBase} is {@code REFERENCE_BASED} or {@code REFERENCE_AND_TIME_BASED}.
		 */
		@NotNull
		@DurationMin(days = 0)
		Duration oldReferenceWarningAfter;

		/**
		 * Defines the {@link AnonymizationBase} for determining whether a user can be anonymized and thus finally
		 * eliminated after his "soft deletion".
		 */
		@NotNull
		AnonymizationBase anonymizationBase;

		enum AnonymizationBase {
			/**
			 * Anonymize after a fixed duration.
			 */
			TIME_BASED,
			/**
			 * Anonymize after no relevant reference is left.
			 */
			REFERENCE_BASED,
			/**
			 * Anonymize after no relevant reference is left or (as fallback) after a fixed duration.
			 */
			REFERENCE_OR_TIME_BASED;

			boolean isTimeBased() {
				return this == TIME_BASED || this == REFERENCE_OR_TIME_BASED;
			}

			boolean isReferenceBased() {
				return this == REFERENCE_BASED || this == REFERENCE_OR_TIME_BASED;
			}
		}
	}
}
