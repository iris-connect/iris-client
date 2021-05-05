package iris.client_bff.data_request.cases.web.dto;

import lombok.Data;

@Data
public class IndexCaseUpdateDTO {
  private String comment;
  private String externalCaseId;
  private String name;
  private IndexCaseStatusDTO status;
}
