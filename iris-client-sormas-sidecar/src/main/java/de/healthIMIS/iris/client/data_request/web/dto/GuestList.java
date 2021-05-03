package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A collection of guests who attended a queried event or location in the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64! (&#x60;dataToTransport&#x60; in the general description of the API.)
 */
@ApiModel(description = "A collection of guests who attended a queried event or location in the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64! (`dataToTransport` in the general description of the API.)")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class GuestList   {
  @JsonProperty("guests")
  @Valid
  private List<Guest> guests = new ArrayList<>();

  @JsonProperty("dataProvider")
  private GuestListDataProvider dataProvider;

  @JsonProperty("additionalInformation")
  private String additionalInformation;

  @JsonProperty("startDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private Instant startDate;

  @JsonProperty("endDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private Instant endDate;

  public GuestList guests(List<Guest> guests) {
    this.guests = guests;
    return this;
  }

  public GuestList addGuestsItem(Guest guestsItem) {
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

  public GuestList dataProvider(GuestListDataProvider dataProvider) {
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

  public GuestList additionalInformation(String additionalInformation) {
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

  public GuestList startDate(Instant startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Start date/time of attendance for this guest list.
   * @return startDate
  */
  @ApiModelProperty(value = "Start date/time of attendance for this guest list.")

  @Valid

  public Instant getStartDate() {
    return startDate;
  }

  public void setStartDate(Instant startDate) {
    this.startDate = startDate;
  }

  public GuestList endDate(Instant endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * End date/time of attendance for this guest list.
   * @return endDate
  */
  @ApiModelProperty(value = "End date/time of attendance for this guest list.")

  @Valid

  public Instant getEndDate() {
    return endDate;
  }

  public void setEndDate(Instant endDate) {
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
    GuestList guestList = (GuestList) o;
    return Objects.equals(this.guests, guestList.guests) &&
        Objects.equals(this.dataProvider, guestList.dataProvider) &&
        Objects.equals(this.additionalInformation, guestList.additionalInformation) &&
        Objects.equals(this.startDate, guestList.startDate) &&
        Objects.equals(this.endDate, guestList.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guests, dataProvider, additionalInformation, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestList {\n");
    
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

