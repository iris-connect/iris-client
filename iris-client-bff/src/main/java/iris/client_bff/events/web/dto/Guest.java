package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.web.dto.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * A guest who attended a queried event or location in the queried time.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Guest extends Person {

  private GuestAttendanceInformation attendanceInformation;
  private Boolean identityChecked;
}
