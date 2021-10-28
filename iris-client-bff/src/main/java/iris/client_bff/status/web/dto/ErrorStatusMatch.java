package iris.client_bff.status.web.dto;

import lombok.*;

import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public final class ErrorStatusMatch {

    private Pattern match;

    private AppStatus appStatus;

}
