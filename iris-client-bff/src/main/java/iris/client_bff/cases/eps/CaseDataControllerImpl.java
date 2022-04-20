package iris.client_bff.cases.eps;

import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;
import iris.client_bff.config.SuspiciouslyCaseRequestProperties;
import iris.client_bff.core.validation.AttackDetector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class CaseDataControllerImpl implements CaseDataController {

	private final CaseDataSubmissionService submissionService;
	private final SuspiciouslyCaseRequestProperties suspiciouslyRequest;
	private final AttackDetector attackDetector;

	@Override
	public String submitContactAndEventData(
			String dataAuthorizationToken,
			Contacts contacts,
			Events events,
			CaseDataProvider dataProvider) {

		validatePostLimit(contacts.getContactPersons(), "proxy", "contacts");
		validatePostLimit(events.getEvents(), "proxy", "events");

		log.trace("Start submission {}", dataAuthorizationToken);

		return submissionService.validateAndSaveData(dataAuthorizationToken, contacts, events, dataProvider);
	}

	private void validatePostLimit(List<?> list, String client, String type) {

		if (attackDetector.isPostOutOfLimit(list, client,
				suspiciouslyRequest.getDataBlockingThreshold(), suspiciouslyRequest.getDataWarningThreshold(),
				type)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request content exceeded blocking limit!");
		}
	}
}
