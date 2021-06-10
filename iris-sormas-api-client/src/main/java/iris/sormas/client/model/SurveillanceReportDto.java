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
import iris.sormas.client.model.FacilityReferenceDto;
import iris.sormas.client.model.FacilityType;
import iris.sormas.client.model.RegionReferenceDto;
import iris.sormas.client.model.ReportingType;
import iris.sormas.client.model.UserReferenceDto;
import java.time.OffsetDateTime;
import java.time.Instant;
/**
 * SurveillanceReportDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class SurveillanceReportDto {
  @JsonProperty("creationDate")
  private Instant creationDate = null;

  @JsonProperty("changeDate")
  private Instant changeDate = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("pseudonymized")
  private Boolean pseudonymized = null;

  @JsonProperty("reportingType")
  private ReportingType reportingType = null;

  @JsonProperty("creatingUser")
  private UserReferenceDto creatingUser = null;

  @JsonProperty("reportDate")
  private Instant reportDate = null;

  @JsonProperty("dateOfDiagnosis")
  private Instant dateOfDiagnosis = null;

  @JsonProperty("facilityRegion")
  private RegionReferenceDto facilityRegion = null;

  @JsonProperty("facilityDistrict")
  private DistrictReferenceDto facilityDistrict = null;

  @JsonProperty("facilityType")
  private FacilityType facilityType = null;

  @JsonProperty("facility")
  private FacilityReferenceDto facility = null;

  @JsonProperty("facilityDetails")
  private String facilityDetails = null;

  @JsonProperty("notificationDetails")
  private String notificationDetails = null;

  @JsonProperty("caze")
  private CaseReferenceDto caze = null;

  public SurveillanceReportDto creationDate(Instant creationDate) {
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

  public SurveillanceReportDto changeDate(Instant changeDate) {
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

  public SurveillanceReportDto uuid(String uuid) {
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

  public SurveillanceReportDto pseudonymized(Boolean pseudonymized) {
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

  public SurveillanceReportDto reportingType(ReportingType reportingType) {
    this.reportingType = reportingType;
    return this;
  }

   /**
   * Get reportingType
   * @return reportingType
  **/
  @Schema(description = "")
  public ReportingType getReportingType() {
    return reportingType;
  }

  public void setReportingType(ReportingType reportingType) {
    this.reportingType = reportingType;
  }

  public SurveillanceReportDto creatingUser(UserReferenceDto creatingUser) {
    this.creatingUser = creatingUser;
    return this;
  }

   /**
   * Get creatingUser
   * @return creatingUser
  **/
  @Schema(description = "")
  public UserReferenceDto getCreatingUser() {
    return creatingUser;
  }

  public void setCreatingUser(UserReferenceDto creatingUser) {
    this.creatingUser = creatingUser;
  }

  public SurveillanceReportDto reportDate(Instant reportDate) {
    this.reportDate = reportDate;
    return this;
  }

   /**
   * Get reportDate
   * @return reportDate
  **/
  @Schema(description = "")
  public Instant getReportDate() {
    return reportDate;
  }

  public void setReportDate(Instant reportDate) {
    this.reportDate = reportDate;
  }

  public SurveillanceReportDto dateOfDiagnosis(Instant dateOfDiagnosis) {
    this.dateOfDiagnosis = dateOfDiagnosis;
    return this;
  }

   /**
   * Get dateOfDiagnosis
   * @return dateOfDiagnosis
  **/
  @Schema(description = "")
  public Instant getDateOfDiagnosis() {
    return dateOfDiagnosis;
  }

  public void setDateOfDiagnosis(Instant dateOfDiagnosis) {
    this.dateOfDiagnosis = dateOfDiagnosis;
  }

  public SurveillanceReportDto facilityRegion(RegionReferenceDto facilityRegion) {
    this.facilityRegion = facilityRegion;
    return this;
  }

   /**
   * Get facilityRegion
   * @return facilityRegion
  **/
  @Schema(description = "")
  public RegionReferenceDto getFacilityRegion() {
    return facilityRegion;
  }

  public void setFacilityRegion(RegionReferenceDto facilityRegion) {
    this.facilityRegion = facilityRegion;
  }

  public SurveillanceReportDto facilityDistrict(DistrictReferenceDto facilityDistrict) {
    this.facilityDistrict = facilityDistrict;
    return this;
  }

   /**
   * Get facilityDistrict
   * @return facilityDistrict
  **/
  @Schema(description = "")
  public DistrictReferenceDto getFacilityDistrict() {
    return facilityDistrict;
  }

  public void setFacilityDistrict(DistrictReferenceDto facilityDistrict) {
    this.facilityDistrict = facilityDistrict;
  }

  public SurveillanceReportDto facilityType(FacilityType facilityType) {
    this.facilityType = facilityType;
    return this;
  }

   /**
   * Get facilityType
   * @return facilityType
  **/
  @Schema(description = "")
  public FacilityType getFacilityType() {
    return facilityType;
  }

  public void setFacilityType(FacilityType facilityType) {
    this.facilityType = facilityType;
  }

  public SurveillanceReportDto facility(FacilityReferenceDto facility) {
    this.facility = facility;
    return this;
  }

   /**
   * Get facility
   * @return facility
  **/
  @Schema(description = "")
  public FacilityReferenceDto getFacility() {
    return facility;
  }

  public void setFacility(FacilityReferenceDto facility) {
    this.facility = facility;
  }

  public SurveillanceReportDto facilityDetails(String facilityDetails) {
    this.facilityDetails = facilityDetails;
    return this;
  }

   /**
   * Get facilityDetails
   * @return facilityDetails
  **/
  @Schema(description = "")
  public String getFacilityDetails() {
    return facilityDetails;
  }

  public void setFacilityDetails(String facilityDetails) {
    this.facilityDetails = facilityDetails;
  }

  public SurveillanceReportDto notificationDetails(String notificationDetails) {
    this.notificationDetails = notificationDetails;
    return this;
  }

   /**
   * Get notificationDetails
   * @return notificationDetails
  **/
  @Schema(description = "")
  public String getNotificationDetails() {
    return notificationDetails;
  }

  public void setNotificationDetails(String notificationDetails) {
    this.notificationDetails = notificationDetails;
  }

  public SurveillanceReportDto caze(CaseReferenceDto caze) {
    this.caze = caze;
    return this;
  }

   /**
   * Get caze
   * @return caze
  **/
  @Schema(description = "")
  public CaseReferenceDto getCaze() {
    return caze;
  }

  public void setCaze(CaseReferenceDto caze) {
    this.caze = caze;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveillanceReportDto surveillanceReportDto = (SurveillanceReportDto) o;
    return Objects.equals(this.creationDate, surveillanceReportDto.creationDate) &&
        Objects.equals(this.changeDate, surveillanceReportDto.changeDate) &&
        Objects.equals(this.uuid, surveillanceReportDto.uuid) &&
        Objects.equals(this.pseudonymized, surveillanceReportDto.pseudonymized) &&
        Objects.equals(this.reportingType, surveillanceReportDto.reportingType) &&
        Objects.equals(this.creatingUser, surveillanceReportDto.creatingUser) &&
        Objects.equals(this.reportDate, surveillanceReportDto.reportDate) &&
        Objects.equals(this.dateOfDiagnosis, surveillanceReportDto.dateOfDiagnosis) &&
        Objects.equals(this.facilityRegion, surveillanceReportDto.facilityRegion) &&
        Objects.equals(this.facilityDistrict, surveillanceReportDto.facilityDistrict) &&
        Objects.equals(this.facilityType, surveillanceReportDto.facilityType) &&
        Objects.equals(this.facility, surveillanceReportDto.facility) &&
        Objects.equals(this.facilityDetails, surveillanceReportDto.facilityDetails) &&
        Objects.equals(this.notificationDetails, surveillanceReportDto.notificationDetails) &&
        Objects.equals(this.caze, surveillanceReportDto.caze);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, changeDate, uuid, pseudonymized, reportingType, creatingUser, reportDate, dateOfDiagnosis, facilityRegion, facilityDistrict, facilityType, facility, facilityDetails, notificationDetails, caze);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveillanceReportDto {\n");
    
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    pseudonymized: ").append(toIndentedString(pseudonymized)).append("\n");
    sb.append("    reportingType: ").append(toIndentedString(reportingType)).append("\n");
    sb.append("    creatingUser: ").append(toIndentedString(creatingUser)).append("\n");
    sb.append("    reportDate: ").append(toIndentedString(reportDate)).append("\n");
    sb.append("    dateOfDiagnosis: ").append(toIndentedString(dateOfDiagnosis)).append("\n");
    sb.append("    facilityRegion: ").append(toIndentedString(facilityRegion)).append("\n");
    sb.append("    facilityDistrict: ").append(toIndentedString(facilityDistrict)).append("\n");
    sb.append("    facilityType: ").append(toIndentedString(facilityType)).append("\n");
    sb.append("    facility: ").append(toIndentedString(facility)).append("\n");
    sb.append("    facilityDetails: ").append(toIndentedString(facilityDetails)).append("\n");
    sb.append("    notificationDetails: ").append(toIndentedString(notificationDetails)).append("\n");
    sb.append("    caze: ").append(toIndentedString(caze)).append("\n");
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
