/*
 * SORMAS REST API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.61.0-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package iris.sormas.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import iris.sormas.client.model.CaseJurisdictionDto;
import iris.sormas.client.model.ContactJurisdictionDto;
import iris.sormas.client.model.EventParticipantJurisdictionDto;
/**
 * SampleJurisdictionDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class SampleJurisdictionDto {
  @JsonProperty("reportingUserUuid")
  private String reportingUserUuid = null;

  @JsonProperty("caseJurisdiction")
  private CaseJurisdictionDto caseJurisdiction = null;

  @JsonProperty("contactJurisdiction")
  private ContactJurisdictionDto contactJurisdiction = null;

  @JsonProperty("eventParticipantJurisdiction")
  private EventParticipantJurisdictionDto eventParticipantJurisdiction = null;

  @JsonProperty("labUuid")
  private String labUuid = null;

  public SampleJurisdictionDto reportingUserUuid(String reportingUserUuid) {
    this.reportingUserUuid = reportingUserUuid;
    return this;
  }

   /**
   * Get reportingUserUuid
   * @return reportingUserUuid
  **/
  @Schema(description = "")
  public String getReportingUserUuid() {
    return reportingUserUuid;
  }

  public void setReportingUserUuid(String reportingUserUuid) {
    this.reportingUserUuid = reportingUserUuid;
  }

  public SampleJurisdictionDto caseJurisdiction(CaseJurisdictionDto caseJurisdiction) {
    this.caseJurisdiction = caseJurisdiction;
    return this;
  }

   /**
   * Get caseJurisdiction
   * @return caseJurisdiction
  **/
  @Schema(description = "")
  public CaseJurisdictionDto getCaseJurisdiction() {
    return caseJurisdiction;
  }

  public void setCaseJurisdiction(CaseJurisdictionDto caseJurisdiction) {
    this.caseJurisdiction = caseJurisdiction;
  }

  public SampleJurisdictionDto contactJurisdiction(ContactJurisdictionDto contactJurisdiction) {
    this.contactJurisdiction = contactJurisdiction;
    return this;
  }

   /**
   * Get contactJurisdiction
   * @return contactJurisdiction
  **/
  @Schema(description = "")
  public ContactJurisdictionDto getContactJurisdiction() {
    return contactJurisdiction;
  }

  public void setContactJurisdiction(ContactJurisdictionDto contactJurisdiction) {
    this.contactJurisdiction = contactJurisdiction;
  }

  public SampleJurisdictionDto eventParticipantJurisdiction(EventParticipantJurisdictionDto eventParticipantJurisdiction) {
    this.eventParticipantJurisdiction = eventParticipantJurisdiction;
    return this;
  }

   /**
   * Get eventParticipantJurisdiction
   * @return eventParticipantJurisdiction
  **/
  @Schema(description = "")
  public EventParticipantJurisdictionDto getEventParticipantJurisdiction() {
    return eventParticipantJurisdiction;
  }

  public void setEventParticipantJurisdiction(EventParticipantJurisdictionDto eventParticipantJurisdiction) {
    this.eventParticipantJurisdiction = eventParticipantJurisdiction;
  }

  public SampleJurisdictionDto labUuid(String labUuid) {
    this.labUuid = labUuid;
    return this;
  }

   /**
   * Get labUuid
   * @return labUuid
  **/
  @Schema(description = "")
  public String getLabUuid() {
    return labUuid;
  }

  public void setLabUuid(String labUuid) {
    this.labUuid = labUuid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SampleJurisdictionDto sampleJurisdictionDto = (SampleJurisdictionDto) o;
    return Objects.equals(this.reportingUserUuid, sampleJurisdictionDto.reportingUserUuid) &&
        Objects.equals(this.caseJurisdiction, sampleJurisdictionDto.caseJurisdiction) &&
        Objects.equals(this.contactJurisdiction, sampleJurisdictionDto.contactJurisdiction) &&
        Objects.equals(this.eventParticipantJurisdiction, sampleJurisdictionDto.eventParticipantJurisdiction) &&
        Objects.equals(this.labUuid, sampleJurisdictionDto.labUuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportingUserUuid, caseJurisdiction, contactJurisdiction, eventParticipantJurisdiction, labUuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SampleJurisdictionDto {\n");
    
    sb.append("    reportingUserUuid: ").append(toIndentedString(reportingUserUuid)).append("\n");
    sb.append("    caseJurisdiction: ").append(toIndentedString(caseJurisdiction)).append("\n");
    sb.append("    contactJurisdiction: ").append(toIndentedString(contactJurisdiction)).append("\n");
    sb.append("    eventParticipantJurisdiction: ").append(toIndentedString(eventParticipantJurisdiction)).append("\n");
    sb.append("    labUuid: ").append(toIndentedString(labUuid)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}