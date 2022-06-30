package iris.client_bff.dbms;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestDataInitializer;
import iris.client_bff.cases.CaseDataRequestRepository;
import iris.client_bff.cases.CaseDataRequestService;
import iris.client_bff.cases.CaseDataSubmissionRepository;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataRequestsDataInitializer;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

/**
 * @author Jens Kutzsche
 */
abstract class DatabasesystemIT {

	@Autowired
	private EventDataRequestRepository eventRequests;
	@Autowired
	private EventDataSubmissionRepository eventSubmissions;
	@Autowired
	private CaseDataRequestRepository caseRequests;
	@Autowired
	private CaseDataSubmissionRepository caseSubmissions;
	@Autowired
	private EventDataRequestService eventReqService;
	@Autowired
	private CaseDataRequestService caseReqService;
	@Autowired
	private IrisMessageService irisMessageService;
	@Autowired
	private IrisMessageFolderRepository irisMessageFolderRepository;

	@Test
	void eventRequests() {

		assertThat(eventRequests.findAll()).hasSize(3)
				.extracting(EventDataRequest::getRefId).contains(EventDataRequestsDataInitializer.REQ_ID_1.toString(),
						EventDataRequestsDataInitializer.REQ_ID_2.toString(), EventDataRequestsDataInitializer.REQ_ID_3.toString());

		assertThat(eventRequests.getCountSinceDate(Instant.ofEpochMilli(0l))).isEqualTo(3);

		assertThat(eventRequests.getCountWithStatus(Status.DATA_REQUESTED)).isEqualTo(3);

		assertThat(eventRequests.findByStatus(Status.DATA_REQUESTED, Pageable.ofSize(10)).getTotalElements()).isEqualTo(3);
	}

	@Test
	void eventRequestSearch() {

		assertThat(eventReqService.search(EventDataRequestsDataInitializer.REQ_ID_1.toString(), Pageable.ofSize(10))
				.getTotalElements())
						.isEqualTo(1);
		assertThat(eventReqService.search("Anfrage", Pageable.ofSize(10)).getTotalElements()).isEqualTo(3);
		assertThat(eventReqService.search("aaa", Pageable.ofSize(10)).getTotalElements()).isZero();

		assertThat(eventReqService.search(Status.DATA_REQUESTED, "Anfrage", Pageable.ofSize(10))
				.getTotalElements()).isEqualTo(3);
		assertThat(eventReqService.search(Status.DATA_REQUESTED, "aaa", Pageable.ofSize(10))
				.getTotalElements()).isZero();
		assertThat(eventReqService.search(Status.CLOSED, "Anfrage", Pageable.ofSize(10))
				.getTotalElements()).isZero();
	}

	@Test
	void eventSubmissions() {

		assertThat(eventSubmissions.findAllByRequest(EventDataRequestsDataInitializer.DATA_REQUEST_1).toList()).hasSize(1);
	}

	@Test
	void caseRequests() {

		assertThat(caseRequests.findAll()).hasSize(3)
				.extracting(CaseDataRequest::getRefId).contains(CaseDataRequestDataInitializer.REQ_ID_1.toString(),
						CaseDataRequestDataInitializer.REQ_ID_2.toString(), CaseDataRequestDataInitializer.REQ_ID_3.toString());

		assertThat(caseRequests.getCountSinceDate(Instant.ofEpochMilli(0l))).isEqualTo(3);

		assertThat(caseRequests.getCountWithStatus(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED))
				.isEqualTo(3);

		assertThat(
				caseRequests.findByStatus(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED, Pageable.ofSize(10))
						.getTotalElements()).isEqualTo(3);
	}

	@Test
	void caseRequestSearch() {

		assertThat(caseReqService.search(CaseDataRequestDataInitializer.REQ_ID_1.toString(), Pageable.ofSize(10))
				.getTotalElements()).isEqualTo(1);
		assertThat(caseReqService.search("Anfrage", Pageable.ofSize(10)).getTotalElements()).isEqualTo(3);
		assertThat(caseReqService.search("aaa", Pageable.ofSize(10)).getTotalElements()).isZero();

		assertThat(caseReqService.search(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED, "Anfrage",
				Pageable.ofSize(10))
				.getTotalElements()).isEqualTo(3);
		assertThat(caseReqService.search(iris.client_bff.cases.CaseDataRequest.Status.DATA_REQUESTED, "aaa",
				Pageable.ofSize(10))
				.getTotalElements()).isZero();
		assertThat(caseReqService.search(iris.client_bff.cases.CaseDataRequest.Status.CLOSED, "Anfrage",
				Pageable.ofSize(10))
				.getTotalElements()).isZero();
	}

	@Test
	void caseSubmissions() {

		assertThat(caseSubmissions.findAllByRequest(CaseDataRequestDataInitializer.DATA_REQUEST_1).toList()).hasSize(1);
	}

	@Test
	void irisMessages() {

		List<IrisMessageFolder> inboxRootFolders = irisMessageFolderRepository.findAll();
		assertThat(inboxRootFolders).hasSize(3);

		Optional<IrisMessageFolder> inboxRootFolder = irisMessageFolderRepository
				.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX);
		assertThat(inboxRootFolder.isPresent()).isTrue();
		assertThat(irisMessageService.search(inboxRootFolder.get().getId(), null, null).toList()).hasSize(2);

		Optional<IrisMessageFolder> outboxRootFolder = irisMessageFolderRepository
				.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
		assertThat(outboxRootFolder.isPresent()).isTrue();
		assertThat(irisMessageService.search(outboxRootFolder.get().getId(), null, null).toList()).hasSize(1);

		List<IrisMessageFolder> inboxNestedFolders = irisMessageFolderRepository
				.findAllByParentFolder(inboxRootFolder.get().getId());
		assertThat(inboxNestedFolders).hasSize(1);

		assertThat(irisMessageService.search(inboxNestedFolders.get(0).getId(), null, null).toList()).hasSize(1);

		assertThat(irisMessageService.getCountUnread()).isEqualTo(3);

		assertThat(irisMessageService.getCountUnreadByFolderId(inboxRootFolder.get().getId())).isEqualTo(2);
		assertThat(irisMessageService.getCountUnreadByFolderId(outboxRootFolder.get().getId())).isEqualTo(0);
		assertThat(irisMessageService.getCountUnreadByFolderId(inboxNestedFolders.get(0).getId())).isEqualTo(1);

	}

}
