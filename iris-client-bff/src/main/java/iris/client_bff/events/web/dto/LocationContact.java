package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Kontaktperson des Standorts
 */
@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class LocationContact {

  private String officialName;
  private String representative;
  private LocationAddress address;
  private String ownerEmail;
  private String email;
  private String phone;
}
