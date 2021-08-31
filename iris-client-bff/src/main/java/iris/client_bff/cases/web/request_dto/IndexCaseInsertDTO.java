package iris.client_bff.cases.web.request_dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class IndexCaseInsertDTO {
  private String comment;
  private String externalCaseId;
  private String name;

  @NotNull
  private Instant start;

  private Instant end;
}
