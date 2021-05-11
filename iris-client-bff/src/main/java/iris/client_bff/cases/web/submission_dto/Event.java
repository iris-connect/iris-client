package iris.client_bff.cases.web.submission_dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.web.dto.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Event {

  private String name;
  private String phone;
  private Address address;
  private String additionalInformation;
}
