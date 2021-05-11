package iris.client_bff.cases.web.submission_dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactPersonAllOfContactInformation {

  private LocalDate firstContactDate;
  private LocalDate lastContactDate;
  private ContactCategory contactCategory;
  private String basicConditions;
}
