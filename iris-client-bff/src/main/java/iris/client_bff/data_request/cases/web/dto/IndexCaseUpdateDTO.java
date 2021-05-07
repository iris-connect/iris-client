package iris.client_bff.data_request.cases.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexCaseUpdateDTO {
  private String comment;
  private String externalCaseId;
  private String name;
  private IndexCaseStatusDTO status;
}
