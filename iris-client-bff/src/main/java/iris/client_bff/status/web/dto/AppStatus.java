package iris.client_bff.status.web.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppStatus {

    public enum Status {
        OK,
        WARNING,
        ERROR;
    }

    private Status status;

    private String message;

}
