package de.healthIMIS.iris.client.data_request.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Additional informations about the contact(s) with the queried person.
 */
@ApiModel(description = "Additional informations about the contact(s) with the queried person.")
@Getter
@Setter
@NoArgsConstructor
public class ContactPersonAllOfContactInformation   {

  private LocalDate firstContactDate;

  private LocalDate lastContactDate;

  private ContactCategory contactCategory;

  private String basicConditions;

}

