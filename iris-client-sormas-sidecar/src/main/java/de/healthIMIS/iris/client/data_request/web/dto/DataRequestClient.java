package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

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

  @NotBlank
  private String name;

  @NotBlank
  private String externalRequestId;

  @NotNull
  private ZonedDateTime start;

  @NotNull
  private ZonedDateTime end;

  private String requestDetails;

}

