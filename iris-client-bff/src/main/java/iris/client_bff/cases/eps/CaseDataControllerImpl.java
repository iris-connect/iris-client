package iris.client_bff.cases.eps;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Events;
import java.util.UUID;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class CaseDataControllerImpl implements CaseDataController {

	private final CaseDataSubmissionService submissionService;

	@Override
	public String submitContactAndEventData(UUID dataAuthorizationToken, @Valid Contacts contacts, @Valid Events events,
			@Valid CaseDataProvider dataProvider) {

		log.trace("Start submission {}: contacts = {}; events = {}; dataProvider = {}", dataAuthorizationToken, contacts, events, dataProvider);

		return submissionService.validateAndSaveData(dataAuthorizationToken, contacts, events, dataProvider);
	}
}
