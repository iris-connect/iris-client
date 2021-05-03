package de.healthIMIS.iris.client.data_request.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Extended person data type for a guest who attended a queried event or location in the queried time.
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "Extended person data type for a guest who attended a queried event or location in the queried time.")
public class Guest   {

  private String firstName;

  private String lastName;

  private LocalDate dateOfBirth;

  private Sex sex = Sex.UNKNOWN;

  private String email;

  private String phone;

  private String mobilePhone;

  private Address address;

  private GuestAllOfAttendanceInformation attendanceInformation;

  private Boolean identityChecked;

}

