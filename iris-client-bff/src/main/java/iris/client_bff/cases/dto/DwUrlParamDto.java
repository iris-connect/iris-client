package iris.client_bff.cases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import static iris.client_bff.core.log.LogHelper.obfuscateAtStart8;

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
        return String.format("dataAuthorizationToken=%s; connectionAuthorizationToken=%s; HD zip code=%s", d, c, p);
    }

    public String toStringWithObfuscation() {
        return String.format("dataAuthorizationToken=%s; connectionAuthorizationToken=%s; HD zip code=%s",
                obfuscateAtStart8(d), obfuscateAtStart8(c), p);
    }
}
