package iris.client_bff.cases;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.core.IrisDateTimeProvider;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

/**
 * @author Jens Kutzsche
 * @since 1.4
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class CaseDeleteJobIntegrationTests {

	private final CaseDataRequestRepository caseRequests;
	private final CaseDataSubmissionRepository caseSubmissions;
	private final IrisDateTimeProvider dateTimeProvider;
	private final CaseDeleteJob deleteJob;
	private final Faker faker;

	@Test // Issue #244
	void testDeleteCaseRequests() {

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

		var all = caseRequests.findAll();

		// extra element from data initialization
		assertThat(all).hasSize(5).element(4).satisfies(it -> {
			assertThat(it.getName()).isEqualTo(oldName);
		});
		assertThat(caseSubmissions.findAll()).hasSize(2);

		deleteJob.deleteCaseRequests();

		all = caseRequests.findAll();

		assertThat(all).hasSize(4).extracting(CaseDataRequest::getName).doesNotContain(oldName);
		assertThat(caseSubmissions.findAll()).hasSize(1);
	}

	private CaseDataRequest createRequest(String name, String refId, Instant date) {

		return caseRequests.save(CaseDataRequest.builder()
				.name(name)
				.refId(refId)
				.requestStart(date)
				.requestEnd(date.plus(6, ChronoUnit.HOURS))
				.build());
	}

	private void createSubmission(CaseDataRequest request) {
		caseSubmissions.save(new CaseDataSubmission(request, null, null, null, null, null, null, null));
	}
}
