package iris.client_bff.data_request.events.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A data request with all parameters relevant for the data submission.
 */
@ApiModel(description = "A data request with all parameters relevant for the data submission.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class DataRequest   {
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

  @JsonProperty("comment")
  private String comment;
  
  public DataRequest healthDepartment(String healthDepartment) {
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

  public DataRequest keyOfHealthDepartment(String keyOfHealthDepartment) {
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

  public DataRequest keyReference(String keyReference) {
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

  public DataRequest start(Instant start) {
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

  public DataRequest end(Instant end) {
    this.end = end;
    return this;
  }

  /**
   * The end time for which data should be submitted with this request.
   * @return end
  */
  @ApiModelProperty(value = "The end time for which data should be submitted with this request.")

  @Valid

  public Instant getEnd() {
    return end;
  }

  public void setEnd(Instant end) {
    this.end = end;
  }

  public DataRequest requestDetails(String requestDetails) {
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

  public DataRequest comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Comment from an IRIS user
   * @return comment
  */
  @ApiModelProperty(value = "Comment from an IRIS user")


  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataRequest dataRequest = (DataRequest) o;
    return Objects.equals(this.healthDepartment, dataRequest.healthDepartment) &&
        Objects.equals(this.keyOfHealthDepartment, dataRequest.keyOfHealthDepartment) &&
        Objects.equals(this.keyReference, dataRequest.keyReference) &&
        Objects.equals(this.start, dataRequest.start) &&
        Objects.equals(this.end, dataRequest.end) &&
        Objects.equals(this.requestDetails, dataRequest.requestDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(healthDepartment, keyOfHealthDepartment, keyReference, start, end, requestDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataRequest {\n");
    
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
