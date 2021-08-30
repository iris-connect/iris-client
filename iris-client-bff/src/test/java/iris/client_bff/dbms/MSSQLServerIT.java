package iris.client_bff.dbms;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.cases.CaseDataRequestDataInitializer;
import iris.client_bff.cases.CaseDataRequestRepository;
import iris.client_bff.cases.CaseDataSubmissionRepository;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataRequestsDataInitializer;
import iris.client_bff.events.EventDataSubmissionRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;

@IrisDBMSTest
@ActiveProfiles({ "dev", "inttest", "test", "mssql" })
@RequiredArgsConstructor
class MSSQLServerIT {

	@Container
	public static MSSQLServerContainer<?> msSQLServer = MSSQLServer.getInstance();

	private final EventDataRequestRepository eventRequests;
	private final EventDataSubmissionRepository eventSubmissions;
	private final CaseDataRequestRepository caseRequests;
	private final CaseDataSubmissionRepository caseSubmissions;

	@Test
	void eventRequests() {

		assertThat(eventRequests.findAll()).hasSize(3)
				.extracting(it -> it.getRefId()).contains(EventDataRequestsDataInitializer.REQ_ID_1.toString(),
						EventDataRequestsDataInitializer.REQ_ID_2.toString(), EventDataRequestsDataInitializer.REQ_ID_3.toString());

		assertThat(eventRequests.getCountSinceDate(Instant.ofEpochMilli(0l))).isEqualTo(3);

		assertThat(eventRequests.getCountWithStatus(Status.DATA_REQUESTED)).isEqualTo(3);

		assertThat(eventRequests.findByStatus(Status.DATA_REQUESTED, Pageable.ofSize(10)).getTotalElements()).isEqualTo(3);

		assertThat(eventRequests.findByRefIdContainsOrNameContainsAllIgnoreCase(
				EventDataRequestsDataInitializer.REQ_ID_1.toString(), "aaa", Pageable.ofSize(10)).getTotalElements())
						.isEqualTo(1);
		assertThat(eventRequests.findByRefIdContainsOrNameContainsAllIgnoreCase(
				UUID.randomUUID().toString(), "Anfrage", Pageable.ofSize(10)).getTotalElements())
						.isEqualTo(3);
		assertThat(eventRequests.findByRefIdContainsOrNameContainsAllIgnoreCase(
				UUID.randomUUID().toString(), "aaa", Pageable.ofSize(10)).getTotalElements())
						.isZero();

		assertThat(eventRequests.findByStatusAndSearchByRefIdOrName(Status.DATA_REQUESTED, "Anfrage", Pageable.ofSize(10))
				.getTotalElements()).isEqualTo(3);
		assertThat(eventRequests.findByStatusAndSearchByRefIdOrName(Status.DATA_REQUESTED, "aaa", Pageable.ofSize(10))
				.getTotalElements()).isZero();
		assertThat(eventRequests.findByStatusAndSearchByRefIdOrName(Status.CLOSED, "Anfrage", Pageable.ofSize(10))
				.getTotalElements()).isZero();
	}

	@Test
	void eventSubmissions() {

		assertThat(eventSubmissions.findAllByRequest(EventDataRequestsDataInitializer.DATA_REQUEST_1).toList()).hasSize(1);
	}

	@Test
	void caseRequests() {

		assertThat(caseRequests.findAll()).hasSize(3)
				.extracting(it -> it.getRefId()).contains(CaseDataRequestDataInitializer.REQ_ID_1.toString(),
						CaseDataRequestDataInitializer.REQ_ID_2.toString(), CaseDataRequestDataInitializer.REQ_ID_3.toString());

		assertThat(caseRequests.getCountSinceDate(Instant.ofEpochMilli(0l))).isEqualTo(3);

		assertThat(caseRequests.getCountWithStatus(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED))
				.isEqualTo(3);

		assertThat(
				caseRequests.findByStatus(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED, Pageable.ofSize(10))
						.getTotalElements()).isEqualTo(3);

		assertThat(caseRequests.findByRefIdContainsOrNameContainsAllIgnoreCase(
				CaseDataRequestDataInitializer.REQ_ID_1.toString(), "aaa", Pageable.ofSize(10)).getTotalElements())
						.isEqualTo(1);
		assertThat(caseRequests.findByRefIdContainsOrNameContainsAllIgnoreCase(
				UUID.randomUUID().toString(), "Anfrage", Pageable.ofSize(10)).getTotalElements())
						.isEqualTo(3);
		assertThat(caseRequests.findByRefIdContainsOrNameContainsAllIgnoreCase(
				UUID.randomUUID().toString(), "aaa", Pageable.ofSize(10)).getTotalElements())
						.isZero();

		assertThat(caseRequests
				.findByStatusAndSearchByRefIdOrName(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED, "Anfrage",
						Pageable.ofSize(10))
				.getTotalElements()).isEqualTo(3);
		assertThat(caseRequests
				.findByStatusAndSearchByRefIdOrName(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED, "aaa",
						Pageable.ofSize(10))
				.getTotalElements()).isZero();
		assertThat(caseRequests
				.findByStatusAndSearchByRefIdOrName(iris.client_bff.cases.CaseDataRequest.Status.CLOSED, "Anfrage",
						Pageable.ofSize(10))
				.getTotalElements()).isZero();
	}

	@Test
	void caseSubmissions() {

		assertThat(caseSubmissions.findAllByRequest(CaseDataRequestDataInitializer.DATA_REQUEST_1).toList()).hasSize(1);
	}
}
