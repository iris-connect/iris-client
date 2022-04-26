package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.MemoryAppender;
import iris.client_bff.auth.db.UserAccountAuthentication;
import iris.client_bff.core.IrisDateTimeProvider;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.model.Location.LocationIdentifier;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.entities.UserRole;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
@Tag("users")
@Tag("delete-job")
@DisplayName("IT of delete job for users")
class UserDeleteJobIntegrationTests {

	private final UserAccountsRepository users;
	private final IrisDateTimeProvider dateTimeProvider;
	private final UserDeleteJob deleteJob;
	private final PasswordEncoder passwordEncoder;
	private final EventDataRequestRepository events;

	private MemoryAppender memoryAppender;
	private UserAccount user;

	@BeforeEach
	void setup() {

		Logger logger = (Logger) LoggerFactory.getLogger(UserDeleteJob.class);
		memoryAppender = new MemoryAppender();
		memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(Level.INFO);
		logger.addAppender(memoryAppender);
		memoryAppender.start();

		user = createUser("test");
	}

	@AfterEach
	void cleanUp() {
		users.delete(user);
	}

	@Test
	@DisplayName("deleteUsers: without marked users ⇒ delete nothing")
	void deleteUsers_WithoutMarked_DeleteNothing() {

		var usersSize = users.findAll().size();

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName).contains(user.getLastName());
		assertThat(user.getUserName()).isEqualTo(user.getUserName());

		deleteJob.deleteUsers();

		assertThat(users.findAll()).hasSize(usersSize).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
	}

	@Test
	@DisplayName("deleteUsers: marked user without referenz ⇒ delete")
	void deleteUsers_WithoutRef_Delete() {

		var usersSize = users.findAll().size();

		user.markDeleted();
		users.save(user);

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
		assertThat(user.getUserName()).isNotEqualTo("test");

		deleteJob.deleteUsers();

		assertThat(users.findAll()).hasSize(usersSize - 1).extracting(UserAccount::getLastName)
				.doesNotContain(user.getLastName());

		assertThat(memoryAppender.countEventsForLogger(UserDeleteJob.class)).isNotZero();
		assertThat(memoryAppender.contains(
				String.format("User with ID = %s deleted!", user.getId().toString()),
				Level.INFO)).isTrue();
	}

	@Test
	@DisplayName("deleteUsers: user with referenz + before warning period ⇒ delete nothing + without warning")
	void deleteUsers_WithRefBeforeWarnPeriod_DeleteNothingWithoutWarning() {

		var usersSize = users.findAll().size();

		var request = createReferenzToUserAndDeleateUser(185);

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
		assertThat(user.getUserName()).isNotEqualTo("test");

		deleteJob.deleteUsers();

		assertThat(users.findAll()).hasSize(usersSize).extracting(UserAccount::getLastName)
				.contains(user.getLastName());

		assertThat(memoryAppender.countEventsForLogger(UserDeleteJob.class)).isZero();

		// clean up
		events.delete(request);
	}

	@Test
	@DisplayName("deleteUsers: user with referenz + after warning period ⇒ delete nothing + with warning")
	void deleteUsers_WithRefAfterWarnPeriod_DeleteNothingWithWarning() {

		var usersSize = users.findAll().size();

		var request = createReferenzToUserAndDeleateUser(187);

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
		assertThat(user.getUserName()).isNotEqualTo("test");

		deleteJob.deleteUsers();

		assertThat(users.findAll()).hasSize(usersSize).extracting(UserAccount::getLastName)
				.contains(user.getLastName());

		assertThat(memoryAppender.countEventsForLogger(UserDeleteJob.class)).isNotZero();
		assertThat(memoryAppender.contains(
				String.format("User with ID = %s could not be deleted!", user.getId().toString()),
				Level.WARN)).isTrue();

		// clean up
		events.delete(request);
	}

	private UserAccount createUser(String name) {

		var userAccount = new UserAccount();
		userAccount.setUserName(name);
		userAccount.setPassword(passwordEncoder.encode(name));
		userAccount.setFirstName(name);
		userAccount.setLastName(name);
		userAccount.setRole(UserRole.ADMIN);

		users.save(userAccount);

		return userAccount;
	}

	private EventDataRequest createReferenzToUserAndDeleateUser(int durationDays) {

		// set security context to include correct createdBy metadata
		SecurityContextHolder.getContext()
				.setAuthentication(new UserAccountAuthentication(user, true, null));

		// set time
		var timeShift = Duration.ofDays(durationDays);
		var time = Instant.now().minus(timeShift);

		dateTimeProvider.setDelta(timeShift);
		var request = createRequest("request", "refId", time);

		// mark deleted
		user.markDeleted();
		user.setDeletedAt(time);
		users.save(user);

		dateTimeProvider.reset();

		return request;
	}

	private EventDataRequest createRequest(String name, String refId, Instant date) {

		var location = new Location(LocationIdentifier.random(), "providerId", "locationId", null,
				null, null, null, null, null, null, null, null, null);

		return events.save(EventDataRequest.builder()
				.name(name)
				.refId(refId)
				.requestStart(date)
				.requestEnd(date.plus(6, ChronoUnit.HOURS))
				.location(location)
				.build());
	}
}
