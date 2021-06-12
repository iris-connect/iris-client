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
import iris.sormas.client.model.CaseReferenceDto;
import iris.sormas.client.model.DistrictReferenceDto;
import iris.sormas.client.model.EventReferenceDto;
import iris.sormas.client.model.PersonDto;
import iris.sormas.client.model.RegionReferenceDto;
import iris.sormas.client.model.SormasToSormasOriginInfoDto;
import iris.sormas.client.model.UserReferenceDto;
import iris.sormas.client.model.VaccinationInfoDto;
import java.time.OffsetDateTime;
import java.time.Instant;
/**
 * EventParticipantDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class EventParticipantDto {
  @JsonProperty("creationDate")
  private Instant creationDate = null;

  @JsonProperty("changeDate")
  private Instant changeDate = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("pseudonymized")
  private Boolean pseudonymized = null;

  @JsonProperty("reportingUser")
  private UserReferenceDto reportingUser = null;

  @JsonProperty("event")
  private EventReferenceDto event = null;

  @JsonProperty("person")
  private PersonDto person = null;

  @JsonProperty("involvementDescription")
  private String involvementDescription = null;

  @JsonProperty("resultingCase")
  private CaseReferenceDto resultingCase = null;

  @JsonProperty("region")
  private RegionReferenceDto region = null;

  @JsonProperty("district")
  private DistrictReferenceDto district = null;

  @JsonProperty("vaccinationInfo")
  private VaccinationInfoDto vaccinationInfo = null;

  @JsonProperty("sormasToSormasOriginInfo")
  private SormasToSormasOriginInfoDto sormasToSormasOriginInfo = null;

  @JsonProperty("ownershipHandedOver")
  private Boolean ownershipHandedOver = null;

  public EventParticipantDto creationDate(Instant creationDate) {
    this.creationDate = creationDate;
    return this;
  }

   /**
   * Get creationDate
   * @return creationDate
  **/
  @Schema(description = "")
  public Instant getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Instant creationDate) {
    this.creationDate = creationDate;
  }

  public EventParticipantDto changeDate(Instant changeDate) {
    this.changeDate = changeDate;
    return this;
  }

   /**
   * Get changeDate
   * @return changeDate
  **/
  @Schema(description = "")
  public Instant getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Instant changeDate) {
    this.changeDate = changeDate;
  }

  public EventParticipantDto uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   * Get uuid
   * @return uuid
  **/
  @Schema(description = "")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public EventParticipantDto pseudonymized(Boolean pseudonymized) {
    this.pseudonymized = pseudonymized;
    return this;
  }

   /**
   * Get pseudonymized
   * @return pseudonymized
  **/
  @Schema(description = "")
  public Boolean isPseudonymized() {
    return pseudonymized;
  }

  public void setPseudonymized(Boolean pseudonymized) {
    this.pseudonymized = pseudonymized;
  }

  public EventParticipantDto reportingUser(UserReferenceDto reportingUser) {
    this.reportingUser = reportingUser;
    return this;
  }

   /**
   * Get reportingUser
   * @return reportingUser
  **/
  @Schema(description = "")
  public UserReferenceDto getReportingUser() {
    return reportingUser;
  }

  public void setReportingUser(UserReferenceDto reportingUser) {
    this.reportingUser = reportingUser;
  }

  public EventParticipantDto event(EventReferenceDto event) {
    this.event = event;
    return this;
  }

   /**
   * Get event
   * @return event
  **/
  @Schema(required = true, description = "")
  public EventReferenceDto getEvent() {
    return event;
  }

  public void setEvent(EventReferenceDto event) {
    this.event = event;
  }

  public EventParticipantDto person(PersonDto person) {
    this.person = person;
    return this;
  }

   /**
   * Get person
   * @return person
  **/
  @Schema(required = true, description = "")
  public PersonDto getPerson() {
    return person;
  }

  public void setPerson(PersonDto person) {
    this.person = person;
  }

  public EventParticipantDto involvementDescription(String involvementDescription) {
    this.involvementDescription = involvementDescription;
    return this;
  }

   /**
   * Get involvementDescription
   * @return involvementDescription
  **/
  @Schema(description = "")
  public String getInvolvementDescription() {
    return involvementDescription;
  }

  public void setInvolvementDescription(String involvementDescription) {
    this.involvementDescription = involvementDescription;
  }

  public EventParticipantDto resultingCase(CaseReferenceDto resultingCase) {
    this.resultingCase = resultingCase;
    return this;
  }

   /**
   * Get resultingCase
   * @return resultingCase
  **/
  @Schema(description = "")
  public CaseReferenceDto getResultingCase() {
    return resultingCase;
  }

  public void setResultingCase(CaseReferenceDto resultingCase) {
    this.resultingCase = resultingCase;
  }

  public EventParticipantDto region(RegionReferenceDto region) {
    this.region = region;
    return this;
  }

   /**
   * Get region
   * @return region
  **/
  @Schema(description = "")
  public RegionReferenceDto getRegion() {
    return region;
  }

  public void setRegion(RegionReferenceDto region) {
    this.region = region;
  }

  public EventParticipantDto district(DistrictReferenceDto district) {
    this.district = district;
    return this;
  }

   /**
   * Get district
   * @return district
  **/
  @Schema(description = "")
  public DistrictReferenceDto getDistrict() {
    return district;
  }

  public void setDistrict(DistrictReferenceDto district) {
    this.district = district;
  }

  public EventParticipantDto vaccinationInfo(VaccinationInfoDto vaccinationInfo) {
    this.vaccinationInfo = vaccinationInfo;
    return this;
  }

   /**
   * Get vaccinationInfo
   * @return vaccinationInfo
  **/
  @Schema(description = "")
  public VaccinationInfoDto getVaccinationInfo() {
    return vaccinationInfo;
  }

  public void setVaccinationInfo(VaccinationInfoDto vaccinationInfo) {
    this.vaccinationInfo = vaccinationInfo;
  }

  public EventParticipantDto sormasToSormasOriginInfo(SormasToSormasOriginInfoDto sormasToSormasOriginInfo) {
    this.sormasToSormasOriginInfo = sormasToSormasOriginInfo;
    return this;
  }

   /**
   * Get sormasToSormasOriginInfo
   * @return sormasToSormasOriginInfo
  **/
  @Schema(description = "")
  public SormasToSormasOriginInfoDto getSormasToSormasOriginInfo() {
    return sormasToSormasOriginInfo;
  }

  public void setSormasToSormasOriginInfo(SormasToSormasOriginInfoDto sormasToSormasOriginInfo) {
    this.sormasToSormasOriginInfo = sormasToSormasOriginInfo;
  }

  public EventParticipantDto ownershipHandedOver(Boolean ownershipHandedOver) {
    this.ownershipHandedOver = ownershipHandedOver;
    return this;
  }

   /**
   * Get ownershipHandedOver
   * @return ownershipHandedOver
  **/
  @Schema(description = "")
  public Boolean isOwnershipHandedOver() {
    return ownershipHandedOver;
  }

  public void setOwnershipHandedOver(Boolean ownershipHandedOver) {
    this.ownershipHandedOver = ownershipHandedOver;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventParticipantDto eventParticipantDto = (EventParticipantDto) o;
    return Objects.equals(this.creationDate, eventParticipantDto.creationDate) &&
        Objects.equals(this.changeDate, eventParticipantDto.changeDate) &&
        Objects.equals(this.uuid, eventParticipantDto.uuid) &&
        Objects.equals(this.pseudonymized, eventParticipantDto.pseudonymized) &&
        Objects.equals(this.reportingUser, eventParticipantDto.reportingUser) &&
        Objects.equals(this.event, eventParticipantDto.event) &&
        Objects.equals(this.person, eventParticipantDto.person) &&
        Objects.equals(this.involvementDescription, eventParticipantDto.involvementDescription) &&
        Objects.equals(this.resultingCase, eventParticipantDto.resultingCase) &&
        Objects.equals(this.region, eventParticipantDto.region) &&
        Objects.equals(this.district, eventParticipantDto.district) &&
        Objects.equals(this.vaccinationInfo, eventParticipantDto.vaccinationInfo) &&
        Objects.equals(this.sormasToSormasOriginInfo, eventParticipantDto.sormasToSormasOriginInfo) &&
        Objects.equals(this.ownershipHandedOver, eventParticipantDto.ownershipHandedOver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, changeDate, uuid, pseudonymized, reportingUser, event, person, involvementDescription, resultingCase, region, district, vaccinationInfo, sormasToSormasOriginInfo, ownershipHandedOver);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventParticipantDto {\n");
    
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    pseudonymized: ").append(toIndentedString(pseudonymized)).append("\n");
    sb.append("    reportingUser: ").append(toIndentedString(reportingUser)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    person: ").append(toIndentedString(person)).append("\n");
    sb.append("    involvementDescription: ").append(toIndentedString(involvementDescription)).append("\n");
    sb.append("    resultingCase: ").append(toIndentedString(resultingCase)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    district: ").append(toIndentedString(district)).append("\n");
    sb.append("    vaccinationInfo: ").append(toIndentedString(vaccinationInfo)).append("\n");
    sb.append("    sormasToSormasOriginInfo: ").append(toIndentedString(sormasToSormasOriginInfo)).append("\n");
    sb.append("    ownershipHandedOver: ").append(toIndentedString(ownershipHandedOver)).append("\n");
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