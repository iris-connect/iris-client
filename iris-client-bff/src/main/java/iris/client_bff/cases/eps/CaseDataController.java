package iris.client_bff.cases.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.DataProvider;
import iris.client_bff.cases.eps.dto.Events;
import java.util.UUID;
import javax.validation.Valid;

public interface CaseDataController {
	String submitContactAndEventData(
			@JsonRpcParam(value = "dataAuthorizationToken") UUID dataAuthorizationToken,
			@Valid @JsonRpcParam(value = "contacts") Contacts contacts,
			@Valid @JsonRpcParam(value = "events") Events events,
			@Valid @JsonRpcParam(value = "dataProvider") DataProvider dataProvider
	);
}
