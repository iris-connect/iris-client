package de.healthIMIS.iris.client.data_request.web.dto;

import io.swagger.annotations.ApiModel;

/**
 * An event, location or occasion visited by the queried person during the queried time.
 */
@ApiModel(description = "An event, location or occasion visited by the queried person during the queried time.")
public class Event   {

  private String name;

  private String phone;

  private Address address;

  private String additionalInformation;

}

