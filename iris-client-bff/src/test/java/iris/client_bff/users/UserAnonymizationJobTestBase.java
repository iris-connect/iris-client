package iris.client_bff.users;

import static org.assertj.core.api.Assertions.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import iris.client_bff.MemoryAppender;
import iris.client_bff.core.utils.IrisDateTimeProvider;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.model.Location.LocationIdentifier;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Jens Kutzsche
 */
class UserAnonymizationJobTestBase {

	protected final UserAccountsRepository users;
	private final IrisDateTimeProvider dateTimeProvider;
	protected final UserAnonymizationJob anonymizationJob;
	protected final EventDataRequestRepository events;

	protected MemoryAppender memoryAppender;
	protected UserAccount user;
	private UserAccount user2;

	public UserAnonymizationJobTestBase(UserAccountsRepository users,
			IrisDateTimeProvider dateTimeProvider,
			UserAnonymizationJob anonymizationJob,
			EventDataRequestRepository events) {

		this.users = users;
		this.dateTimeProvider = dateTimeProvider;
		this.anonymizationJob = anonymizationJob;
		this.events = events;
	}

	@BeforeEach
	void setup() {

		Logger logger = (Logger) LoggerFactory.getLogger(UserAnonymizationJob.class);
		memoryAppender = new MemoryAppender();
		memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(Level.INFO);
		logger.addAppender(memoryAppender);
		memoryAppender.start();

		user = users.save(createUser("test"));

		// set security context to include correct createdBy metadata
		SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, null));

		// Create second user to verify if the reference in this entity will be ignored.
		user2 = users.save(createUser("test2"));
	}

	@AfterEach
	void cleanUp() {
		users.delete(user2);
		users.delete(user);
	}

	@Test
	@DisplayName("processUsers: without marked users ⇒ anonymize nothing")
	void processUsers_WithoutMarked_AnonymizeNothing() {

		var usersSize = users.findAll().size();

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName).contains(user.getLastName());
		assertThat(user.getUserName()).isEqualTo(user.getUserName());

		anonymizationJob.processUsers();

		assertThat(users.findAll()).hasSize(usersSize).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
	}

	@Test
	@DisplayName("processUsers: user with referenz + before anonymization and warning periods ⇒ anonymize nothing + without warning")
	void processUsers_WithRefBeforeWarnPeriod_AnonymizeNothingWithoutWarning() {

		var usersSize = users.findAll().size();

		var request = createReferenzToUserAndDeleateUser(185);

		// extra elements from data initialization
		assertThat(users.findAll()).extracting(UserAccount::getLastName)
				.contains(user.getLastName());
		assertThat(user.getUserName()).isNotEqualTo("test");

		anonymizationJob.processUsers();

		assertThat(users.findAll()).hasSize(usersSize).extracting(UserAccount::getLastName)
				.contains(user.getLastName());

		assertThat(memoryAppender.countEventsForLogger(UserAnonymizationJob.class)).isZero();

		// clean up
		events.delete(request);
	}

	UserAccount createUser(String name) {

		var userAccount = new UserAccount();
		userAccount.setUserName(name);
		userAccount.setPassword(name);
		userAccount.setFirstName(name);
		userAccount.setLastName(name);
		userAccount.setRole(UserRole.ADMIN);

		return userAccount;
	}

	protected EventDataRequest createReferenzToUserAndDeleateUser(int durationDays) {

		// set security context to include correct createdBy metadata
		SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, null));

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

	EventDataRequest createRequest(String name, String refId, Instant date) {

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
