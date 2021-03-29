package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The data request that will be sent by the FE.
 */
@ApiModel(description = "The data request that will be sent by the FE.")
@NoArgsConstructor
@Setter
@Getter
public class DataRequestClient   {

  private String locationId;

  private String providerId;

  private String name;

  private String externalRequestId;

  private OffsetDateTime start;

  private OffsetDateTime end;

  private String requestDetails;

}

