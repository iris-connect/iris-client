package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A collection of contact persons who had contact with the queried person during the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!
 */
@ApiModel(description = "A collection of contact persons who had contact with the queried person during the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class ContactPersonList   {
  @JsonProperty("contactPersons")
  @Valid
  private List<ContactPerson> contactPersons = new ArrayList<>();

  @JsonProperty("startDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @JsonProperty("endDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  public ContactPersonList contactPersons(List<ContactPerson> contactPersons) {
    this.contactPersons = contactPersons;
    return this;
  }

  public ContactPersonList addContactPersonsItem(ContactPerson contactPersonsItem) {
    this.contactPersons.add(contactPersonsItem);
    return this;
  }

  /**
   * Get contactPersons
   * @return contactPersons
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<ContactPerson> getContactPersons() {
    return contactPersons;
  }

  public void setContactPersons(List<ContactPerson> contactPersons) {
    this.contactPersons = contactPersons;
  }

  public ContactPersonList startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Start date of contacts for this list.
   * @return startDate
  */
  @ApiModelProperty(value = "Start date of contacts for this list.")

  @Valid

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public ContactPersonList endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * End date of contacts for this list.
   * @return endDate
  */
  @ApiModelProperty(value = "End date of contacts for this list.")

  @Valid

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
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
    ContactPersonList contactPersonList = (ContactPersonList) o;
    return Objects.equals(this.contactPersons, contactPersonList.contactPersons) &&
        Objects.equals(this.startDate, contactPersonList.startDate) &&
        Objects.equals(this.endDate, contactPersonList.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactPersons, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContactPersonList {\n");
    
    sb.append("    contactPersons: ").append(toIndentedString(contactPersons)).append("\n");
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

