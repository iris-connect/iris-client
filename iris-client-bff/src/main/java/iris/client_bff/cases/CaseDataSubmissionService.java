package iris.client_bff.cases;

import static iris.client_bff.cases.CaseDataRequest.Status.*;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.model.CaseEvent;
import iris.client_bff.cases.model.Contact;
import iris.client_bff.cases.web.IndexCaseMapper;
import iris.client_bff.cases.web.submission_dto.ContactsAndEvents;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CaseDataSubmissionService {

	private final CaseDataSubmissionRepository submissionRepo;
	private final CaseDataRequestRepository requestRepo;
	private final ProxyServiceClient proxyClient;
	private final CaseEmailProvider caseEmailProvider;
	private final AlertService alertService;
	private final CaseDataSubmissionMapper mapper;

	// NOTE: Necessary to map here and use Transactional annotation, because of
	// https://stackoverflow.com/a/42206232
	@Transactional
	public Optional<ContactsAndEvents> findByRequest(CaseDataRequest request) {
		// TODO clarify which submission to return
		var submission = submissionRepo.findAllByRequest(request).stream().findFirst();
		return submission.map(IndexCaseMapper::mapDataSubmission);
	}

	public String validateAndSaveData(String dataAuthorizationToken, Contacts contacts, Events events,
			CaseDataProvider dataProvider) {
		// Todo check client.getName() vs. providerIdø

		return requestRepo
				.findByIdOrDataAuthorizationToken(DataRequestIdentifier.of(dataAuthorizationToken), dataAuthorizationToken)
				.map(dataRequest -> {

					var requestStatus = dataRequest.getStatus();
					if (ABORTED.equals(requestStatus)) {
						log.trace("Submission {} for aborted case {}", dataAuthorizationToken, dataRequest.getId());
						return "Error: Submission not allowed for case " + dataAuthorizationToken + ". Request was aborted.";
					}
					if (CLOSED.equals(requestStatus)) {
						log.trace("Submission {} for closed case {}", dataAuthorizationToken, dataRequest.getId());
						return "Error: Submission not allowed for case " + dataAuthorizationToken + ". Request already closed.";
					}
					if (DATA_RECEIVED.equals(requestStatus)) {
						log.trace("Submission {} for received case {}", dataAuthorizationToken, dataRequest.getId());
						return "Error: Submission not allowed for case " + dataAuthorizationToken + ". Data already received.";
					}

					save(dataRequest, contacts, events, dataProvider);

					caseEmailProvider.sendDataReceivedEmailAsynchronously(dataRequest);

					try {
						proxyClient.abortAnnouncement(dataRequest.getAnnouncementToken());
					} catch (IRISAnnouncementException e) {
						log.error("Abort announcement for token {} failed", dataRequest.getAnnouncementToken(), e);
					}

					log.trace("Done case submission {}", dataAuthorizationToken);

					return "OK";

				}).orElseGet(() -> {

					alertService.createAlertMessage("Submission for unknown Token - possible attack",
							String.format("Case data submission for unknown data request occurred: (Data Authorization Token = %s)",
									LogHelper.obfuscateAtStart8(dataAuthorizationToken)));

					return "Unknown dataAuthorizationToken: " + dataAuthorizationToken;
				});

	}

	public void save(CaseDataRequest dataRequest, Contacts contacts, Events events, CaseDataProvider dataProvider) {
		Set<Contact> contactsForDb = new HashSet<>();
		if (contacts.getContactPersons() != null) {
			contactsForDb = contacts.getContactPersons().stream()
					.map(mapper::fromContactPersonDto)
					.collect(Collectors.toSet());
		}

		Set<CaseEvent> eventsForDb = new HashSet<>();
		if (events.getEvents() != null) {
			eventsForDb = events.getEvents().stream().map(mapper::fromEventDto).collect(Collectors.toSet());
		}

		var submission = new CaseDataSubmission(
				dataRequest,
				contactsForDb,
				contacts.getStartDate(),
				contacts.getEndDate(),
				eventsForDb,
				events.getStartDate(),
				events.getStartDate(),
				mapper.fromCaseDataProviderDto(dataProvider));

		contactsForDb.forEach(it -> it.setSubmission(submission));
		eventsForDb.forEach(it -> it.setSubmission(submission));

		submissionRepo.save(submission);

		dataRequest.setStatus(Status.DATA_RECEIVED);

		requestRepo.save(dataRequest);
	}
}
