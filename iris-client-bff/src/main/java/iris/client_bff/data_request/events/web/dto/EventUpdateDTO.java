package iris.client_bff.data_request.events.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventUpdateDTO {
  private String comment;
  private String externalRequestId;
  private String name;
  private EventStatusDTO status;
}
