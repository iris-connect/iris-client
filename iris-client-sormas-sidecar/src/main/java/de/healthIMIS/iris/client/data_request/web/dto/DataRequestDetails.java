package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DataRequestDetails
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class DataRequestDetails   {
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

  @JsonProperty("guests")
  @Valid
  private List<Guest> guests = new ArrayList<>();

  @JsonProperty("dataProvider")
  private GuestListDataProvider dataProvider;

  @JsonProperty("additionalInformation")
  private String additionalInformation;

  @JsonProperty("startDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startDate;

  @JsonProperty("endDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endDate;

  public DataRequestDetails status(StatusEnum status) {
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

  public DataRequestDetails code(String code) {
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

  public DataRequestDetails name(String name) {
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

  public DataRequestDetails externalRequestId(String externalRequestId) {
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

  public DataRequestDetails start(OffsetDateTime start) {
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

  public DataRequestDetails end(OffsetDateTime end) {
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

  public DataRequestDetails requestDetails(String requestDetails) {
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

  public DataRequestDetails locationInformation(LocationInformation locationInformation) {
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

  public DataRequestDetails guests(List<Guest> guests) {
    this.guests = guests;
    return this;
  }

  public DataRequestDetails addGuestsItem(Guest guestsItem) {
    this.guests.add(guestsItem);
    return this;
  }

  /**
   * Get guests
   * @return guests
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<Guest> getGuests() {
    return guests;
  }

  public void setGuests(List<Guest> guests) {
    this.guests = guests;
  }

  public DataRequestDetails dataProvider(GuestListDataProvider dataProvider) {
    this.dataProvider = dataProvider;
    return this;
  }

  /**
   * Get dataProvider
   * @return dataProvider
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public GuestListDataProvider getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(GuestListDataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  public DataRequestDetails additionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
    return this;
  }

  /**
   * Additional informations about the guest list and the event or location.
   * @return additionalInformation
  */
  @ApiModelProperty(value = "Additional informations about the guest list and the event or location.")


  public String getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public DataRequestDetails startDate(OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Start date/time of attendance for this guest list.
   * @return startDate
  */
  @ApiModelProperty(value = "Start date/time of attendance for this guest list.")

  @Valid

  public OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public DataRequestDetails endDate(OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * End date/time of attendance for this guest list.
   * @return endDate
  */
  @ApiModelProperty(value = "End date/time of attendance for this guest list.")

  @Valid

  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataRequestDetails dataRequestDetails = (DataRequestDetails) o;
    return Objects.equals(this.status, dataRequestDetails.status) &&
        Objects.equals(this.code, dataRequestDetails.code) &&
        Objects.equals(this.name, dataRequestDetails.name) &&
        Objects.equals(this.externalRequestId, dataRequestDetails.externalRequestId) &&
        Objects.equals(this.start, dataRequestDetails.start) &&
        Objects.equals(this.end, dataRequestDetails.end) &&
        Objects.equals(this.requestDetails, dataRequestDetails.requestDetails) &&
        Objects.equals(this.locationInformation, dataRequestDetails.locationInformation) &&
        Objects.equals(this.guests, dataRequestDetails.guests) &&
        Objects.equals(this.dataProvider, dataRequestDetails.dataProvider) &&
        Objects.equals(this.additionalInformation, dataRequestDetails.additionalInformation) &&
        Objects.equals(this.startDate, dataRequestDetails.startDate) &&
        Objects.equals(this.endDate, dataRequestDetails.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, code, name, externalRequestId, start, end, requestDetails, locationInformation, guests, dataProvider, additionalInformation, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataRequestDetails {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    externalRequestId: ").append(toIndentedString(externalRequestId)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    requestDetails: ").append(toIndentedString(requestDetails)).append("\n");
    sb.append("    locationInformation: ").append(toIndentedString(locationInformation)).append("\n");
    sb.append("    guests: ").append(toIndentedString(guests)).append("\n");
    sb.append("    dataProvider: ").append(toIndentedString(dataProvider)).append("\n");
    sb.append("    additionalInformation: ").append(toIndentedString(additionalInformation)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

