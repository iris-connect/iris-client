package iris.client_bff.cases;

import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.model.CaseDataSubmission.DataSubmissionIdentifier;
import iris.client_bff.cases.model.Contact;
import iris.client_bff.cases.model.CaseEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import iris.client_bff.cases.web.submission_dto.ContactsAndEvents;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static iris.client_bff.cases.web.IndexCaseMapper.mapDataSubmission;

@Slf4j
@Service
@AllArgsConstructor
public class CaseDataSubmissionService {

	private final ModelMapper mapper;

	private final CaseDataSubmissionRepository submissionRepo;
	private final CaseDataRequestRepository requestRepo;

	// NOTE: Necessary to map here and use Transactional annotation, because of
	// https://stackoverflow.com/a/42206232
	@Transactional
	public Optional<ContactsAndEvents> findByRequest(CaseDataRequest request) {
		// TODO clarify which submission to return
		var submission =  submissionRepo.findAllByRequest(request).stream().findFirst();
		return submission.map(sub -> mapDataSubmission(sub));
	}

	public String validateAndSaveData(UUID dataAuthorizationToken, Contacts contacts, Events events,
			CaseDataProvider dataProvider) {
		// Todo check client.getName() vs. providerId

		return requestRepo.findById(CaseDataRequest.DataRequestIdentifier.of(dataAuthorizationToken)).map(dataRequest -> {

			if (dataRequest.getStatus().equals(CaseDataRequest.Status.ABORTED)) {
				log.trace("Submission {} for aborted case {}", dataAuthorizationToken, dataRequest.getId());
				return "Error: Submission not allowed for case " + dataAuthorizationToken.toString() + ". Request aborted.";
			}

			save(dataRequest, contacts, events, dataProvider);

			log.trace("Done case submission {}", dataAuthorizationToken);

			return "OK";

		}).orElseGet(() -> {

			// TODO sufficient?
			// probably throw exception

			log.error("Case submission for unknown data request occurred: {}", dataAuthorizationToken);

			return "Unknown dataAuthorizationToken: " + dataAuthorizationToken.toString();
		});

	}

	public void save(CaseDataRequest dataRequest, Contacts contacts, Events events, CaseDataProvider dataProvider) {
		var contactsForDb = contacts.getContactPersons().stream()
				.map(it -> mapper.map(it, Contact.class))
				.collect(Collectors.toSet());

		var eventsForDb = events.getEvents().stream()
				.map(it -> mapper.map(it, CaseEvent.class))
				.collect(Collectors.toSet());

		var dataProviderForDb = mapper.map(dataProvider, iris.client_bff.cases.model.CaseDataProvider.class);
		dataProviderForDb.setDateOfBirth(LocalDate.ofInstant(dataProvider.getDateOfBirth(), ZoneId.of("CET")));

		var submission = new CaseDataSubmission(dataRequest, contactsForDb, contacts.getStartDate(), contacts.getEndDate(),
				eventsForDb, events.getStartDate(), events.getStartDate(), dataProviderForDb);

		 contactsForDb.forEach(it -> it.setSubmission(submission));
		 eventsForDb.forEach(it -> it.setSubmission(submission));

		submissionRepo.save(submission);

		dataRequest.setStatus(CaseDataRequest.Status.DATA_RECEIVED); // TODO: add generic status and save

		requestRepo.save(dataRequest);
	}

}
