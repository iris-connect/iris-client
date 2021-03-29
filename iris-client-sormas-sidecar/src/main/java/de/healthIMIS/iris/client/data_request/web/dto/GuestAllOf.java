package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * GuestAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class GuestAllOf   {
  @JsonProperty("attendanceInformation")
  private GuestAllOfAttendanceInformation attendanceInformation;

  @JsonProperty("identityChecked")
  private Boolean identityChecked;

  public GuestAllOf attendanceInformation(GuestAllOfAttendanceInformation attendanceInformation) {
    this.attendanceInformation = attendanceInformation;
    return this;
  }

  /**
   * Get attendanceInformation
   * @return attendanceInformation
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public GuestAllOfAttendanceInformation getAttendanceInformation() {
    return attendanceInformation;
  }

  public void setAttendanceInformation(GuestAllOfAttendanceInformation attendanceInformation) {
    this.attendanceInformation = attendanceInformation;
  }

  public GuestAllOf identityChecked(Boolean identityChecked) {
    this.identityChecked = identityChecked;
    return this;
  }

  /**
   * The app indicates whether the data are verified with respect to identity (e.g. by phone number) = TRUE or whether they are unverified form inputs = FALSE.
   * @return identityChecked
  */
  @ApiModelProperty(value = "The app indicates whether the data are verified with respect to identity (e.g. by phone number) = TRUE or whether they are unverified form inputs = FALSE.")


  public Boolean getIdentityChecked() {
    return identityChecked;
  }

  public void setIdentityChecked(Boolean identityChecked) {
    this.identityChecked = identityChecked;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestAllOf guestAllOf = (GuestAllOf) o;
    return Objects.equals(this.attendanceInformation, guestAllOf.attendanceInformation) &&
        Objects.equals(this.identityChecked, guestAllOf.identityChecked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attendanceInformation, identityChecked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestAllOf {\n");
    
    sb.append("    attendanceInformation: ").append(toIndentedString(attendanceInformation)).append("\n");
    sb.append("    identityChecked: ").append(toIndentedString(identityChecked)).append("\n");
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

