package iris.client_bff.data_request.cases.web.dto;

import iris.client_bff.data_request.events.web.dto.ContactsAndEvents;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexCaseDetailsDTO {
  private String caseId;
  private String comment;
  private String externalCaseId;
  private String name;
  private Instant start;
  private Instant end;
  private IndexCaseStatusDTO status;
  private ContactsAndEvents submissionData;
}
