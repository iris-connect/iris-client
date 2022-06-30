package iris.client_bff.cases.eps;

import iris.client_bff.cases.eps.dto.CaseDataProvider;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.Events;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.googlecode.jsonrpc4j.JsonRpcParam;

@Validated
public interface CaseDataController {
	String submitContactAndEventData(
			@JsonRpcParam(value = "dataAuthorizationToken") String dataAuthorizationToken,
			@JsonRpcParam(value = "contacts") @NotNull Contacts contacts,
			@JsonRpcParam(value = "events") @NotNull Events events,
			@JsonRpcParam(value = "dataProvider") @NotNull CaseDataProvider dataProvider);
}
