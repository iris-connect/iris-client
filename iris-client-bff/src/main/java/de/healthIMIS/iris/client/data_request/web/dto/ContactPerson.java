package de.healthIMIS.iris.client.data_request.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Extended person data type for contact persons who had contact with the queried person during the queried time.
 */
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Extended person data type for contact persons who had contact with the queried person during the queried time.")
public class ContactPerson   {

  private String firstName;

  private String lastName;

  private LocalDate dateOfBirth;

  private Sex sex = Sex.UNKNOWN;

  private String email;

  private String phone;

  private String mobilePhone;

  private Address address;

  private ContactPersonAllOfWorkPlace workPlace;

  private ContactPersonAllOfContactInformation contactInformation;

}

