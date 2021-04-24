package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

/**
 * All information needed to create a new TracingTicket
 */
@ApiModel(description = "All information needed to create a new TracingTicket")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class LocationDataRequest   {
  @JsonProperty("submissionUri")
  private String submissionUri;

  @JsonProperty("locationId")
  private String locationId;

  @JsonProperty("healthDepartment")
  private String healthDepartment;

  @JsonProperty("keyOfHealthDepartment")
  private String keyOfHealthDepartment;

  @JsonProperty("keyReference")
  private String keyReference;

  @JsonProperty("start")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private Instant start;

  @JsonProperty("end")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private Instant end;

  @JsonProperty("requestDetails")
  private String requestDetails;

  public LocationDataRequest submissionUri(String submissionUri) {
    this.submissionUri = submissionUri;
    return this;
  }

  /**
   * The URI that can be used to submit contact data for this tracing code.
   * @return submissionUri
  */
  @ApiModelProperty(example = "https://iris.inoeg.de/api/data-submissions/{code}/guests", required = true, value = "The URI that can be used to submit contact data for this tracing code.")
  @NotNull


  public String getSubmissionUri() {
    return submissionUri;
  }

  public void setSubmissionUri(String submissionUri) {
    this.submissionUri = submissionUri;
  }

  public LocationDataRequest locationId(String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * The id of the location.
   * @return locationId
  */
  @ApiModelProperty(example = "35b7df90-8670-409a-9375-15a56fd995c1", required = true, value = "The id of the location.")
  @NotNull


  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public LocationDataRequest healthDepartment(String healthDepartment) {
    this.healthDepartment = healthDepartment;
    return this;
  }

  /**
   * Name of the requesting health department.
   * @return healthDepartment
  */
  @ApiModelProperty(required = true, value = "Name of the requesting health department.")
  @NotNull


  public String getHealthDepartment() {
    return healthDepartment;
  }

  public void setHealthDepartment(String healthDepartment) {
    this.healthDepartment = healthDepartment;
  }

  public LocationDataRequest keyOfHealthDepartment(String keyOfHealthDepartment) {
    this.keyOfHealthDepartment = keyOfHealthDepartment;
    return this;
  }

  /**
   * The key of the requesting health department that must be used for encryption. The key is encoded with Base64.
   * @return keyOfHealthDepartment
  */
  @ApiModelProperty(required = true, value = "The key of the requesting health department that must be used for encryption. The key is encoded with Base64.")
  @NotNull


  public String getKeyOfHealthDepartment() {
    return keyOfHealthDepartment;
  }

  public void setKeyOfHealthDepartment(String keyOfHealthDepartment) {
    this.keyOfHealthDepartment = keyOfHealthDepartment;
  }

  public LocationDataRequest keyReference(String keyReference) {
    this.keyReference = keyReference;
    return this;
  }

  /**
   * Reference id of the given key. This reference must be included in the submission in order to identify the correct private key for decryption at the health department.
   * @return keyReference
  */
  @ApiModelProperty(required = true, value = "Reference id of the given key. This reference must be included in the submission in order to identify the correct private key for decryption at the health department.")
  @NotNull


  public String getKeyReference() {
    return keyReference;
  }

  public void setKeyReference(String keyReference) {
    this.keyReference = keyReference;
  }

  public LocationDataRequest start(Instant start) {
    this.start = start;
    return this;
  }

  /**
   * The start time for which data should be submitted with this request.
   * @return start
  */
  @ApiModelProperty(required = true, value = "The start time for which data should be submitted with this request.")
  @NotNull

  @Valid

  public Instant getStart() {
    return start;
  }

  public void setStart(Instant start) {
    this.start = start;
  }

  public LocationDataRequest end(Instant end) {
    this.end = end;
    return this;
  }

  /**
   * The end time for which data should be submitted with this request.
   * @return end
  */
  @ApiModelProperty(required = true, value = "The end time for which data should be submitted with this request.")
  @NotNull

  @Valid

  public Instant getEnd() {
    return end;
  }

  public void setEnd(Instant end) {
    this.end = end;
  }

  public LocationDataRequest requestDetails(String requestDetails) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDataRequest locationDataRequest = (LocationDataRequest) o;
    return Objects.equals(this.submissionUri, locationDataRequest.submissionUri) &&
        Objects.equals(this.locationId, locationDataRequest.locationId) &&
        Objects.equals(this.healthDepartment, locationDataRequest.healthDepartment) &&
        Objects.equals(this.keyOfHealthDepartment, locationDataRequest.keyOfHealthDepartment) &&
        Objects.equals(this.keyReference, locationDataRequest.keyReference) &&
        Objects.equals(this.start, locationDataRequest.start) &&
        Objects.equals(this.end, locationDataRequest.end) &&
        Objects.equals(this.requestDetails, locationDataRequest.requestDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(submissionUri, locationId, healthDepartment, keyOfHealthDepartment, keyReference, start, end, requestDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationDataRequest {\n");
    
    sb.append("    submissionUri: ").append(toIndentedString(submissionUri)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    healthDepartment: ").append(toIndentedString(healthDepartment)).append("\n");
    sb.append("    keyOfHealthDepartment: ").append(toIndentedString(keyOfHealthDepartment)).append("\n");
    sb.append("    keyReference: ").append(toIndentedString(keyReference)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    requestDetails: ").append(toIndentedString(requestDetails)).append("\n");
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

