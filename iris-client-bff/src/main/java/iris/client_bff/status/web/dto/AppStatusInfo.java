package iris.client_bff.status.web.dto;

import iris.client_bff.status.eps.dto.Ping;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppStatusInfo {

    private Ping info;

    private AppStatus.Status status;

    private String message;

}
