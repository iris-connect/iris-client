package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * GuestsSubmissionAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class GuestsSubmissionAllOf   {
  @JsonProperty("encryptedData")
  private GuestList encryptedData;

  public GuestsSubmissionAllOf encryptedData(GuestList encryptedData) {
    this.encryptedData = encryptedData;
    return this;
  }

  /**
   * Get encryptedData
   * @return encryptedData
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public GuestList getEncryptedData() {
    return encryptedData;
  }

  public void setEncryptedData(GuestList encryptedData) {
    this.encryptedData = encryptedData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestsSubmissionAllOf guestsSubmissionAllOf = (GuestsSubmissionAllOf) o;
    return Objects.equals(this.encryptedData, guestsSubmissionAllOf.encryptedData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(encryptedData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestsSubmissionAllOf {\n");
    
    sb.append("    encryptedData: ").append(toIndentedString(encryptedData)).append("\n");
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

