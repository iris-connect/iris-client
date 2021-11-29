package iris.client_bff.cases;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.core.IrisDateTimeProvider;
import iris.client_bff.core.token.IdentifierToken;
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
class CaseDeleteJobIntegrationTests {

	private final CaseDataRequestRepository caseRequests;
	private final CaseDataSubmissionRepository caseSubmissions;
	private final IrisDateTimeProvider dateTimeProvider;
	private final CaseDeleteJob deleteJob;
	private final Faker faker;

	@Test // Issue #244
	void testDeleteCaseRequests() {

		var requestsSize = caseRequests.findAll().size();
		var submissionSize = caseSubmissions.findAll().size();

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
		assertThat(caseRequests.findAll()).hasSize(requestsSize + 2).extracting(CaseDataRequest::getName).contains(oldName);
		assertThat(caseSubmissions.findAll()).hasSize(submissionSize + 2);

		deleteJob.deleteCaseRequests();

		assertThat(caseRequests.findAll()).hasSize(requestsSize + 1).extracting(CaseDataRequest::getName)
				.doesNotContain(oldName);
		assertThat(caseSubmissions.findAll()).hasSize(submissionSize + 1);
	}

	private CaseDataRequest createRequest(String name, String refId, Instant date) {

		var token = IdentifierToken.builder().readableToken("readableToken")
				.connectionAuthorizationToken(refId).dataAuthorizationToken(refId).build();

		return caseRequests.save(CaseDataRequest.builder()
				.name(name)
				.refId(refId)
				.requestStart(date)
				.requestEnd(date.plus(6, ChronoUnit.HOURS))
				.identifierToken(token)
				.build());
	}

	private void createSubmission(CaseDataRequest request) {
		caseSubmissions.save(new CaseDataSubmission(request, null, null, null, null, null, null, null));
	}
}
