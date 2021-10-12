package iris.client_bff.status.eps.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ping {

    private String version;

    private ServerInfo serverInfo;

}
