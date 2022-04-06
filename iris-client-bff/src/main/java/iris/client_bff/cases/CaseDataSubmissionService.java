package iris.client_bff.cases;

import static iris.client_bff.cases.CaseDataRequest.Status.*;

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

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CaseDataSubmissionService {

	private final ModelMapper mapper;

	private final CaseDataSubmissionRepository submissionRepo;
	private final CaseDataRequestRepository requestRepo;
	private final ProxyServiceClient proxyClient;
	private final CaseEmailProvider caseEmailProvider;
	private final AlertService alertService;

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
				.findByIdOrDataAuthorizationToken(CaseDataRequest.DataRequestIdentifier.of(dataAuthorizationToken),
						dataAuthorizationToken)
				.map(dataRequest -> {

					var requestStatus = dataRequest.getStatus();
					if (requestStatus.equals(ABORTED)) {
						log.trace("Submission {} for aborted case {}", dataAuthorizationToken, dataRequest.getId());
						return "Error: Submission not allowed for case " + dataAuthorizationToken + ". Request was aborted.";
					} else if (requestStatus.equals(CLOSED)) {
						log.trace("Submission {} for closed case {}", dataAuthorizationToken, dataRequest.getId());
						return "Error: Submission not allowed for case " + dataAuthorizationToken + ". Request already closed.";
					} else if (requestStatus.equals(DATA_RECEIVED)) {
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
			contactsForDb = contacts.getContactPersons().stream().map(it -> {
				var mapped = mapper.map(it, Contact.class);

				Optional.ofNullable(it.getContactInformation()).ifPresent(contactInformation -> {
					mapped.setBasicConditions(contactInformation.getBasicConditions());
					if (contactInformation.getContactCategory() != null) {
						mapped.setContactCategory(Contact.ContactCategory.valueOf(contactInformation.getContactCategory().name()));
					}
					mapped.setFirstContactDate(contactInformation.getFirstContactDate());
					mapped.setLastContactDate(contactInformation.getLastContactDate());
				});

				return mapped;
			}).collect(Collectors.toSet());
		}

		Set<CaseEvent> eventsForDb = new HashSet<>();
		if (events.getEvents() != null) {
			eventsForDb = events.getEvents().stream().map(it -> mapper.map(it, CaseEvent.class)).collect(Collectors.toSet());
		}

		var dataProviderForDb = mapper.map(dataProvider, iris.client_bff.cases.model.CaseDataProvider.class);

		var submission = new CaseDataSubmission(
				dataRequest,
				contactsForDb,
				contacts.getStartDate(),
				contacts.getEndDate(),
				eventsForDb,
				events.getStartDate(),
				events.getStartDate(),
				dataProviderForDb);

		contactsForDb.forEach(it -> it.setSubmission(submission));
		eventsForDb.forEach(it -> it.setSubmission(submission));

		submissionRepo.save(submission);

		dataRequest.setStatus(Status.DATA_RECEIVED);

		requestRepo.save(dataRequest);
	}

}
