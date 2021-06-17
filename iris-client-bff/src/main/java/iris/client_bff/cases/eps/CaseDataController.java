package iris.client_bff.cases.eps;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import javax.validation.Valid;

public interface CaseDataController {
	String another(@Valid @JsonRpcParam(value = "name") String name);
}
