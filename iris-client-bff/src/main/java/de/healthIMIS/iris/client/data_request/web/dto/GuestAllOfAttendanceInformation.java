package de.healthIMIS.iris.client.data_request.web.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GuestAllOfAttendanceInformation
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class GuestAllOfAttendanceInformation   {
  @JsonProperty("descriptionOfParticipation")
  private String descriptionOfParticipation;

  @JsonProperty("attendFrom")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private Instant attendFrom;

  @JsonProperty("attendTo")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private Instant attendTo;

  @JsonProperty("additionalInformation")
  private String additionalInformation;

  public GuestAllOfAttendanceInformation descriptionOfParticipation(String descriptionOfParticipation) {
    this.descriptionOfParticipation = descriptionOfParticipation;
    return this;
  }

  /**
   * Description of the type of participation.
   * @return descriptionOfParticipation
  */
  @ApiModelProperty(example = "Guest or Staff", value = "Description of the type of participation.")


  public String getDescriptionOfParticipation() {
    return descriptionOfParticipation;
  }

  public void setDescriptionOfParticipation(String descriptionOfParticipation) {
    this.descriptionOfParticipation = descriptionOfParticipation;
  }

  public GuestAllOfAttendanceInformation attendFrom(Instant attendFrom) {
    this.attendFrom = attendFrom;
    return this;
  }

  /**
   * Start date/time of attendance of this guest.
   * @return attendFrom
  */
  @ApiModelProperty(required = true, value = "Start date/time of attendance of this guest.")
  @NotNull

  @Valid

  public Instant getAttendFrom() {
    return attendFrom;
  }

  public void setAttendFrom(Instant attendFrom) {
    this.attendFrom = attendFrom;
  }

  public GuestAllOfAttendanceInformation attendTo(Instant attendTo) {
    this.attendTo = attendTo;
    return this;
  }

  /**
   * End date/time of attendance of this guest.
   * @return attendTo
  */
  @ApiModelProperty(required = true, value = "End date/time of attendance of this guest.")
  @NotNull

  @Valid

  public Instant getAttendTo() {
    return attendTo;
  }

  public void setAttendTo(Instant attendTo) {
    this.attendTo = attendTo;
  }

  public GuestAllOfAttendanceInformation additionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
    return this;
  }

  /**
   * Additional informations about the attendance.
   * @return additionalInformation
  */
  @ApiModelProperty(example = "Table: 3; Floor: 2", value = "Additional informations about the attendance.")


  public String getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestAllOfAttendanceInformation guestAllOfAttendanceInformation = (GuestAllOfAttendanceInformation) o;
    return Objects.equals(this.descriptionOfParticipation, guestAllOfAttendanceInformation.descriptionOfParticipation) &&
        Objects.equals(this.attendFrom, guestAllOfAttendanceInformation.attendFrom) &&
        Objects.equals(this.attendTo, guestAllOfAttendanceInformation.attendTo) &&
        Objects.equals(this.additionalInformation, guestAllOfAttendanceInformation.additionalInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descriptionOfParticipation, attendFrom, attendTo, additionalInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestAllOfAttendanceInformation {\n");
    
    sb.append("    descriptionOfParticipation: ").append(toIndentedString(descriptionOfParticipation)).append("\n");
    sb.append("    attendFrom: ").append(toIndentedString(attendFrom)).append("\n");
    sb.append("    attendTo: ").append(toIndentedString(attendTo)).append("\n");
    sb.append("    additionalInformation: ").append(toIndentedString(additionalInformation)).append("\n");
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

