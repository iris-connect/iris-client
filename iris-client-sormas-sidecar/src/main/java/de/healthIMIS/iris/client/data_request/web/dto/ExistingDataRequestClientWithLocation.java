package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * ExistingDataRequestClientWithLocation
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class ExistingDataRequestClientWithLocation   {
  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    DATA_REQUESTED("DATA_REQUESTED"),
    
    DATA_RECEIVED("DATA_RECEIVED"),
    
    CLOSED("CLOSED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("status")
  private StatusEnum status;

  @JsonProperty("code")
  private String code;

  @JsonProperty("name")
  private String name;

  @JsonProperty("externalRequestId")
  private String externalRequestId;

  @JsonProperty("start")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime start;

  @JsonProperty("end")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime end;

  @JsonProperty("requestDetails")
  private String requestDetails;

  @JsonProperty("locationInformation")
  private LocationInformation locationInformation;

  public ExistingDataRequestClientWithLocation status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @ApiModelProperty(value = "")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ExistingDataRequestClientWithLocation code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Code for DataRequest
   * @return code
  */
  @ApiModelProperty(value = "Code for DataRequest")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ExistingDataRequestClientWithLocation name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Friendly name of the request to be identified easily by GA
   * @return name
  */
  @ApiModelProperty(value = "Friendly name of the request to be identified easily by GA")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ExistingDataRequestClientWithLocation externalRequestId(String externalRequestId) {
    this.externalRequestId = externalRequestId;
    return this;
  }

  /**
   * External ID outside of IRIS
   * @return externalRequestId
  */
  @ApiModelProperty(value = "External ID outside of IRIS")


  public String getExternalRequestId() {
    return externalRequestId;
  }

  public void setExternalRequestId(String externalRequestId) {
    this.externalRequestId = externalRequestId;
  }

  public ExistingDataRequestClientWithLocation start(OffsetDateTime start) {
    this.start = start;
    return this;
  }

  /**
   * The start time for which data should be submitted with this request.
   * @return start
  */
  @ApiModelProperty(value = "The start time for which data should be submitted with this request.")

  @Valid

  public OffsetDateTime getStart() {
    return start;
  }

  public void setStart(OffsetDateTime start) {
    this.start = start;
  }

  public ExistingDataRequestClientWithLocation end(OffsetDateTime end) {
    this.end = end;
    return this;
  }

  /**
   * The end time for which data should be submitted with this request.
   * @return end
  */
  @ApiModelProperty(value = "The end time for which data should be submitted with this request.")

  @Valid

  public OffsetDateTime getEnd() {
    return end;
  }

  public void setEnd(OffsetDateTime end) {
    this.end = end;
  }

  public ExistingDataRequestClientWithLocation requestDetails(String requestDetails) {
    this.requestDetails = requestDetails;
    return this;
  }

  /**
   * Details of the data request, specifying it in more detail and narrowing down the data to be provided (e.g. table and environment, seat, rank, ...).
   * @return requestDetails
  */
  @ApiModelProperty(value = "Details of the data request, specifying it in more detail and narrowing down the data to be provided (e.g. table and environment, seat, rank, ...).")


  public String getRequestDetails() {
    return requestDetails;
  }

  public void setRequestDetails(String requestDetails) {
    this.requestDetails = requestDetails;
  }

  public ExistingDataRequestClientWithLocation locationInformation(LocationInformation locationInformation) {
    this.locationInformation = locationInformation;
    return this;
  }

  /**
   * Get locationInformation
   * @return locationInformation
  */
  @ApiModelProperty(value = "")

  @Valid

  public LocationInformation getLocationInformation() {
    return locationInformation;
  }

  public void setLocationInformation(LocationInformation locationInformation) {
    this.locationInformation = locationInformation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExistingDataRequestClientWithLocation existingDataRequestClientWithLocation = (ExistingDataRequestClientWithLocation) o;
    return Objects.equals(this.status, existingDataRequestClientWithLocation.status) &&
        Objects.equals(this.code, existingDataRequestClientWithLocation.code) &&
        Objects.equals(this.name, existingDataRequestClientWithLocation.name) &&
        Objects.equals(this.externalRequestId, existingDataRequestClientWithLocation.externalRequestId) &&
        Objects.equals(this.start, existingDataRequestClientWithLocation.start) &&
        Objects.equals(this.end, existingDataRequestClientWithLocation.end) &&
        Objects.equals(this.requestDetails, existingDataRequestClientWithLocation.requestDetails) &&
        Objects.equals(this.locationInformation, existingDataRequestClientWithLocation.locationInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, code, name, externalRequestId, start, end, requestDetails, locationInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExistingDataRequestClientWithLocation {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    externalRequestId: ").append(toIndentedString(externalRequestId)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    requestDetails: ").append(toIndentedString(requestDetails)).append("\n");
    sb.append("    locationInformation: ").append(toIndentedString(locationInformation)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

