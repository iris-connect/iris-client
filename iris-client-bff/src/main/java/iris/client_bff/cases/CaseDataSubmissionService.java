package iris.client_bff.cases;

import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.cases.model.CaseDataSubmission;
import lombok.AllArgsConstructor;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CaseDataSubmissionService {

	private final ModelMapper mapper;

	private final CaseDataSubmissionRepository submissionRepo;
	private final CaseDataRequestRepository requestRepo;

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
		// TODO: map contacts and events
//		var guests = guestList.getGuests().stream()
//				.map(it -> mapper.map(it, Guest.class))
//				.collect(Collectors.toSet());
//
		var dataProviderForRepo = mapper.map(dataProvider, iris.client_bff.cases.model.CaseDataProvider.class);

		var submission = new CaseDataSubmission(dataRequest, dataProviderForRepo); //TODO: provide contacts, events
//
//		guests.forEach(it -> it.setSubmission(submission));// ToDO: set refs in subtables
//
		submissionRepo.save(submission);

		//dataRequest.setStatus(Status.DATA_RECEIVED); // TODO: add generic status and save

		//requestRepo.save(dataRequest);
	}

}
