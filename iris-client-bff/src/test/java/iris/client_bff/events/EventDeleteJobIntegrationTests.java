package iris.client_bff.events;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.IrisDateTimeProvider;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.model.Location.LocationIdentifier;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class EventDeleteJobIntegrationTests {

	private final EventDataRequestRepository eventRequests;
	private final EventDataSubmissionRepository eventSubmissions;
	private final IrisDateTimeProvider dateTimeProvider;
	private final EventDeleteJob deleteJob;
	private final Faker faker;

	@Test // Issue #244
	void testDeleteEventRequests() {

		var requestsSize = eventRequests.findAll().size();
		var submissionSize = eventSubmissions.findAll().size();

		// in time
		dateTimeProvider.setDelta(Period.ofMonths(-6));

		var request = createRequest(faker.name().name(), faker.idNumber().valid(),
				faker.date().past(200, 186, TimeUnit.DAYS).toInstant());
		createSubmission(request);

		// to old
		dateTimeProvider.setDelta(Period.ofMonths(-6).minusDays(1));

		var oldName = faker.name().name();

		request = createRequest(oldName, faker.idNumber().valid(), faker.date().past(200, 186, TimeUnit.DAYS).toInstant());
		createSubmission(request);

		dateTimeProvider.reset();

		// extra element from data initialization
		assertThat(eventRequests.findAll()).hasSize(requestsSize + 2).element(4).satisfies(it -> {
			assertThat(it.getName()).isEqualTo(oldName);
		});
		assertThat(eventSubmissions.findAll()).hasSize(submissionSize + 2);

		deleteJob.deleteEventRequests();

		assertThat(eventRequests.findAll()).hasSize(requestsSize + 1).extracting(EventDataRequest::getName)
				.doesNotContain(oldName);
		assertThat(eventSubmissions.findAll()).hasSize(submissionSize + 1);
	}

	private EventDataRequest createRequest(String name, String refId, Instant date) {

		var location = new Location(new LocationIdentifier(), faker.idNumber().valid(), faker.idNumber().valid(), null,
				null, null, null, null, null, null, null, null);

		return eventRequests.save(EventDataRequest.builder()
				.name(name)
				.refId(refId)
				.requestStart(date)
				.requestEnd(date.plus(6, ChronoUnit.HOURS))
				.location(location)
				.build());
	}

	private void createSubmission(EventDataRequest request) {
		eventSubmissions.save(new EventDataSubmission(request, null, null, null, null, null));
	}
}
