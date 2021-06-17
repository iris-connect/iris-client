package iris.client_bff.cases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DwUrlParamDto {
    // dataAuthorizationToken
    private String d;

    // connectionAuthorizationToken (Proxy-URL)
    private String c;

    // PLZ / Zip code of health department
    private String p;

    public String toString() {
        return "dataAuthorizationToken=" + d + "; connectionAuthorizationToken=" + c + "; HD zip code=" + p;
    }
}
