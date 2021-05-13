package iris.client_bff.cases.web.request_dto;

import iris.client_bff.cases.web.submission_dto.ContactsAndEvents;
import lombok.*;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
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
