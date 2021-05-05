package iris.client_bff.data_request.events.web.dto;

import iris.client_bff.data_request.events.web.dto.DataRequestDetails.StatusEnum;
import lombok.Data;

@Data
public class EventUpdateDTO {
  private String comment;
  private String externalCaseId;
  private String name;
  private StatusEnum status;
}
