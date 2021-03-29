package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

/**
 * ContactPersonAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class ContactPersonAllOf   {
  @JsonProperty("workPlace")
  private ContactPersonAllOfWorkPlace workPlace;

  @JsonProperty("contactInformation")
  private ContactPersonAllOfContactInformation contactInformation;

  public ContactPersonAllOf workPlace(ContactPersonAllOfWorkPlace workPlace) {
    this.workPlace = workPlace;
    return this;
  }

  /**
   * Get workPlace
   * @return workPlace
  */
  @ApiModelProperty(value = "")

  @Valid

  public ContactPersonAllOfWorkPlace getWorkPlace() {
    return workPlace;
  }

  public void setWorkPlace(ContactPersonAllOfWorkPlace workPlace) {
    this.workPlace = workPlace;
  }

  public ContactPersonAllOf contactInformation(ContactPersonAllOfContactInformation contactInformation) {
    this.contactInformation = contactInformation;
    return this;
  }

  /**
   * Get contactInformation
   * @return contactInformation
  */
  @ApiModelProperty(value = "")

  @Valid

  public ContactPersonAllOfContactInformation getContactInformation() {
    return contactInformation;
  }

  public void setContactInformation(ContactPersonAllOfContactInformation contactInformation) {
    this.contactInformation = contactInformation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContactPersonAllOf contactPersonAllOf = (ContactPersonAllOf) o;
    return Objects.equals(this.workPlace, contactPersonAllOf.workPlace) &&
        Objects.equals(this.contactInformation, contactPersonAllOf.contactInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(workPlace, contactInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContactPersonAllOf {\n");
    
    sb.append("    workPlace: ").append(toIndentedString(workPlace)).append("\n");
    sb.append("    contactInformation: ").append(toIndentedString(contactInformation)).append("\n");
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

