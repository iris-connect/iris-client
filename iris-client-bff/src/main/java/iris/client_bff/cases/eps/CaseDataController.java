package iris.client_bff.cases.eps;

import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;

import javax.validation.Valid;

import com.googlecode.jsonrpc4j.JsonRpcParam;

public interface CaseDataController {
	String submitContactAndEventData(
			@JsonRpcParam(value = "dataAuthorizationToken") String dataAuthorizationToken,
			@Valid @JsonRpcParam(value = "contacts") Contacts contacts,
			@Valid @JsonRpcParam(value = "events") Events events,
			@Valid @JsonRpcParam(value = "dataProvider") CaseDataProvider dataProvider);
}
