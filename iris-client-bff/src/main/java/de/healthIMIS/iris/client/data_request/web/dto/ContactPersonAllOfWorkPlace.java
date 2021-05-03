package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

/**
 * Additional informations about the work place of the contact person.
 */
@ApiModel(description = "Additional informations about the work place of the contact person.")
public class ContactPersonAllOfWorkPlace   {

  @JsonProperty("name")
  private String name;

  @JsonProperty("pointOfContact")
  private String pointOfContact;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("address")
  private Address address;

}

