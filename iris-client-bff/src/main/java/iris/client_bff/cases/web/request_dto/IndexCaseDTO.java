package iris.client_bff.cases.web.request_dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class IndexCaseDTO {
  private String caseId;
  private String comment;
  private String externalCaseId;
  private String name;
  private Instant start;
  private Instant end;
  private IndexCaseStatusDTO status;
}
