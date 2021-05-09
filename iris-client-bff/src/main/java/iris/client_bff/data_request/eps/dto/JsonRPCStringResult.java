package iris.client_bff.data_request.eps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class JsonRPCStringResult {

    @JsonProperty("_")
    private String status;

}
