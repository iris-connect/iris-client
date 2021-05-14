package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.Sex;
import iris.client_bff.core.web.dto.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * A guest who attended a queried event or location in the queried time.
 */
@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Guest {

  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  @Builder.Default
  private Sex sex = Sex.UNKNOWN;
  private String email;
  private String phone;
  private String mobilePhone;
  private Address address;
  private GuestAttendanceInformation attendanceInformation;
  private Boolean identityChecked;
}
