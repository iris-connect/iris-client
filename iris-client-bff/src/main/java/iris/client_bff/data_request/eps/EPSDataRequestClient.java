package iris.client_bff.data_request.eps;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.data_request.eps.dto.JsonRPCStringResult;
import iris.client_bff.data_request.eps.dto.EventDataRequestDto;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class EPSDataRequestClient {

    private final JsonRpcHttpClient rpcClient;

    public JsonRPCStringResult requestGuestListData(String endpoint, EventDataRequestDto dataRequestDto) throws IRISSearchException {

        try {
            var methodName = endpoint + ".createDataRequest";
            return rpcClient.invoke(methodName, new HashMap<String, Object>() {{
                put("dataRequest", dataRequestDto);
            }}, JsonRPCStringResult.class);
        } catch (Throwable t) {
            throw new IRISSearchException(t);
        }
    }
}
