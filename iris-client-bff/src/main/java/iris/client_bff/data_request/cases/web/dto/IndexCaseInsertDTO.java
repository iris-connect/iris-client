package iris.client_bff.data_request.cases.web.dto;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexCaseInsertDTO {
  private String comment;
  private String externalCaseId;
  private String name;

  @NotNull
  private Instant start;

  private Instant end;
}
