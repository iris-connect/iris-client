package de.healthIMIS.iris.client.data_request.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The data request that will be sent by the FE.
 */
@ApiModel(description = "The data request that will be sent by the FE.")
@NoArgsConstructor
@Setter
@Getter
public class DataRequestClient   {

  @NotBlank
  private String locationId;

  @NotBlank
  private String providerId;

  private String name;

  @NotBlank
  private String externalRequestId;

  @NotNull
  private Instant start;

  @NotNull
  private Instant end;

  private String requestDetails;

}

