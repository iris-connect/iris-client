package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;

import ch.qos.logback.classic.Level;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.utils.IrisDateTimeProvider;
import iris.client_bff.events.EventDataRequestRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@SpringBootTest(properties = { "iris.client.user.anonymization-base=REFERENCE_BASED" })
@Tag("users")
@Tag("job")
@DisplayName("IT of anonymization job for users - REFERENCE_BASED")
class UserAnonymizationJobIntegrationTests extends UserAnonymizationJobTestBase {

	public UserAnonymizationJobIntegrationTests(UserAccountsRepository users,
			IrisDateTimeProvider dateTimeProvider, UserAnonymizationJob anonymizationJob, EventDataRequestRepository events) {
		super(users, dateTimeProvider, anonymizationJob, events);
	}

	@Test
	@DisplayName("processUsers: marked user without referenz ⇒ anonymize")
	void processUsers_WithoutRef_Anonymize() {

		var usersSize = users.findAll().size();

		user.markDeleted();
		users.save(user);

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
		assertThat(user.getUserName()).isNotEqualTo("test");

		anonymizationJob.processUsers();

		assertThat(users.findAll()).hasSize(usersSize).extracting(UserAccount::getLastName)
				.contains(UserAnonymizationJob.ANONYMOUS_TEXT);

		assertThat(memoryAppender.countEventsForLogger(UserAnonymizationJob.class)).isNotZero();
		assertThat(memoryAppender.contains(
				String.format("User with ID = %s anonymized!", user.getId().toString()),
				Level.INFO)).isTrue();

		assertThat(users.findAllUsers()).hasSize(usersSize - 1).extracting(UserAccount::getLastName)
				.doesNotContain(UserAnonymizationJob.ANONYMOUS_TEXT);
		assertThat(users.findUserById(user.getId())).isEmpty();
	}

	@Test
	@DisplayName("processUsers: user with referenz + after warning period ⇒ anonymize nothing + with warning")
	void processUsers_WithRefAfterWarnPeriod_AnonymizeNothingWithWarning() {

		var usersSize = users.findAll().size();

		var request = createReferenzToUserAndDeleateUser(187);

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
		assertThat(user.getUserName()).isNotEqualTo("test");

		anonymizationJob.processUsers();

		assertThat(users.findAll()).hasSize(usersSize).extracting(UserAccount::getLastName)
				.contains(user.getLastName());

		assertThat(memoryAppender.countEventsForLogger(UserAnonymizationJob.class)).isNotZero();
		assertThat(memoryAppender.contains(
				String.format("User with ID = %s could not be anonymized",
						user.getId().toString()),
				Level.WARN)).isTrue();

		// clean up
		events.delete(request);
	}
}
