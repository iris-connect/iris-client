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
import iris.sormas.client.model.AgeAndBirthDateDto;
import iris.sormas.client.model.CaseClassification;
import iris.sormas.client.model.CaseJurisdictionDto;
import iris.sormas.client.model.CaseOutcome;
import iris.sormas.client.model.Disease;
import iris.sormas.client.model.DiseaseVariant;
import iris.sormas.client.model.ExternalShareStatus;
import iris.sormas.client.model.FollowUpStatus;
import iris.sormas.client.model.InvestigationStatus;
import iris.sormas.client.model.PresentCondition;
import iris.sormas.client.model.Sex;
import iris.sormas.client.model.SymptomJournalStatus;
import iris.sormas.client.model.Vaccination;
import java.time.OffsetDateTime;
import java.time.Instant;
/**
 * CaseIndexDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class CaseIndexDto {
  @JsonProperty("pseudonymized")
  private Boolean pseudonymized = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("epidNumber")
  private String epidNumber = null;

  @JsonProperty("externalID")
  private String externalID = null;

  @JsonProperty("externalToken")
  private String externalToken = null;

  @JsonProperty("personFirstName")
  private String personFirstName = null;

  @JsonProperty("personLastName")
  private String personLastName = null;

  @JsonProperty("disease")
  private Disease disease = null;

  @JsonProperty("diseaseVariant")
  private DiseaseVariant diseaseVariant = null;

  @JsonProperty("diseaseDetails")
  private String diseaseDetails = null;

  @JsonProperty("caseClassification")
  private CaseClassification caseClassification = null;

  @JsonProperty("investigationStatus")
  private InvestigationStatus investigationStatus = null;

  @JsonProperty("presentCondition")
  private PresentCondition presentCondition = null;

  @JsonProperty("reportDate")
  private Instant reportDate = null;

  @JsonProperty("creationDate")
  private Instant creationDate = null;

  @JsonProperty("districtName")
  private String districtName = null;

  @JsonProperty("healthFacilityName")
  private String healthFacilityName = null;

  @JsonProperty("pointOfEntryName")
  private String pointOfEntryName = null;

  @JsonProperty("surveillanceOfficerUuid")
  private String surveillanceOfficerUuid = null;

  @JsonProperty("outcome")
  private CaseOutcome outcome = null;

  @JsonProperty("sex")
  private Sex sex = null;

  @JsonProperty("ageAndBirthDate")
  private AgeAndBirthDateDto ageAndBirthDate = null;

  @JsonProperty("completeness")
  private Float completeness = null;

  @JsonProperty("quarantineTo")
  private Instant quarantineTo = null;

  @JsonProperty("followUpStatus")
  private FollowUpStatus followUpStatus = null;

  @JsonProperty("followUpUntil")
  private Instant followUpUntil = null;

  @JsonProperty("symptomJournalStatus")
  private SymptomJournalStatus symptomJournalStatus = null;

  @JsonProperty("vaccination")
  private Vaccination vaccination = null;

  @JsonProperty("visitCount")
  private Integer visitCount = null;

  @JsonProperty("surveillanceToolLastShareDate")
  private Instant surveillanceToolLastShareDate = null;

  @JsonProperty("surveillanceToolShareCount")
  private Long surveillanceToolShareCount = null;

  @JsonProperty("surveillanceToolStatus")
  private ExternalShareStatus surveillanceToolStatus = null;

  @JsonProperty("jurisdiction")
  private CaseJurisdictionDto jurisdiction = null;

  @JsonProperty("responsibleRegionUuid")
  private String responsibleRegionUuid = null;

  @JsonProperty("regionUuid")
  private String regionUuid = null;

  @JsonProperty("responsibleDistrictUuid")
  private String responsibleDistrictUuid = null;

  @JsonProperty("districtUuid")
  private String districtUuid = null;

  public CaseIndexDto pseudonymized(Boolean pseudonymized) {
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

  public CaseIndexDto id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @Schema(description = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CaseIndexDto uuid(String uuid) {
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

  public CaseIndexDto epidNumber(String epidNumber) {
    this.epidNumber = epidNumber;
    return this;
  }

   /**
   * Get epidNumber
   * @return epidNumber
  **/
  @Schema(description = "")
  public String getEpidNumber() {
    return epidNumber;
  }

  public void setEpidNumber(String epidNumber) {
    this.epidNumber = epidNumber;
  }

  public CaseIndexDto externalID(String externalID) {
    this.externalID = externalID;
    return this;
  }

   /**
   * Get externalID
   * @return externalID
  **/
  @Schema(description = "")
  public String getExternalID() {
    return externalID;
  }

  public void setExternalID(String externalID) {
    this.externalID = externalID;
  }

  public CaseIndexDto externalToken(String externalToken) {
    this.externalToken = externalToken;
    return this;
  }

   /**
   * Get externalToken
   * @return externalToken
  **/
  @Schema(description = "")
  public String getExternalToken() {
    return externalToken;
  }

  public void setExternalToken(String externalToken) {
    this.externalToken = externalToken;
  }

  public CaseIndexDto personFirstName(String personFirstName) {
    this.personFirstName = personFirstName;
    return this;
  }

   /**
   * Get personFirstName
   * @return personFirstName
  **/
  @Schema(description = "")
  public String getPersonFirstName() {
    return personFirstName;
  }

  public void setPersonFirstName(String personFirstName) {
    this.personFirstName = personFirstName;
  }

  public CaseIndexDto personLastName(String personLastName) {
    this.personLastName = personLastName;
    return this;
  }

   /**
   * Get personLastName
   * @return personLastName
  **/
  @Schema(description = "")
  public String getPersonLastName() {
    return personLastName;
  }

  public void setPersonLastName(String personLastName) {
    this.personLastName = personLastName;
  }

  public CaseIndexDto disease(Disease disease) {
    this.disease = disease;
    return this;
  }

   /**
   * Get disease
   * @return disease
  **/
  @Schema(description = "")
  public Disease getDisease() {
    return disease;
  }

  public void setDisease(Disease disease) {
    this.disease = disease;
  }

  public CaseIndexDto diseaseVariant(DiseaseVariant diseaseVariant) {
    this.diseaseVariant = diseaseVariant;
    return this;
  }

   /**
   * Get diseaseVariant
   * @return diseaseVariant
  **/
  @Schema(description = "")
  public DiseaseVariant getDiseaseVariant() {
    return diseaseVariant;
  }

  public void setDiseaseVariant(DiseaseVariant diseaseVariant) {
    this.diseaseVariant = diseaseVariant;
  }

  public CaseIndexDto diseaseDetails(String diseaseDetails) {
    this.diseaseDetails = diseaseDetails;
    return this;
  }

   /**
   * Get diseaseDetails
   * @return diseaseDetails
  **/
  @Schema(description = "")
  public String getDiseaseDetails() {
    return diseaseDetails;
  }

  public void setDiseaseDetails(String diseaseDetails) {
    this.diseaseDetails = diseaseDetails;
  }

  public CaseIndexDto caseClassification(CaseClassification caseClassification) {
    this.caseClassification = caseClassification;
    return this;
  }

   /**
   * Get caseClassification
   * @return caseClassification
  **/
  @Schema(description = "")
  public CaseClassification getCaseClassification() {
    return caseClassification;
  }

  public void setCaseClassification(CaseClassification caseClassification) {
    this.caseClassification = caseClassification;
  }

  public CaseIndexDto investigationStatus(InvestigationStatus investigationStatus) {
    this.investigationStatus = investigationStatus;
    return this;
  }

   /**
   * Get investigationStatus
   * @return investigationStatus
  **/
  @Schema(description = "")
  public InvestigationStatus getInvestigationStatus() {
    return investigationStatus;
  }

  public void setInvestigationStatus(InvestigationStatus investigationStatus) {
    this.investigationStatus = investigationStatus;
  }

  public CaseIndexDto presentCondition(PresentCondition presentCondition) {
    this.presentCondition = presentCondition;
    return this;
  }

   /**
   * Get presentCondition
   * @return presentCondition
  **/
  @Schema(description = "")
  public PresentCondition getPresentCondition() {
    return presentCondition;
  }

  public void setPresentCondition(PresentCondition presentCondition) {
    this.presentCondition = presentCondition;
  }

  public CaseIndexDto reportDate(Instant reportDate) {
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

  public CaseIndexDto creationDate(Instant creationDate) {
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

  public CaseIndexDto districtName(String districtName) {
    this.districtName = districtName;
    return this;
  }

   /**
   * Get districtName
   * @return districtName
  **/
  @Schema(description = "")
  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public CaseIndexDto healthFacilityName(String healthFacilityName) {
    this.healthFacilityName = healthFacilityName;
    return this;
  }

   /**
   * Get healthFacilityName
   * @return healthFacilityName
  **/
  @Schema(description = "")
  public String getHealthFacilityName() {
    return healthFacilityName;
  }

  public void setHealthFacilityName(String healthFacilityName) {
    this.healthFacilityName = healthFacilityName;
  }

  public CaseIndexDto pointOfEntryName(String pointOfEntryName) {
    this.pointOfEntryName = pointOfEntryName;
    return this;
  }

   /**
   * Get pointOfEntryName
   * @return pointOfEntryName
  **/
  @Schema(description = "")
  public String getPointOfEntryName() {
    return pointOfEntryName;
  }

  public void setPointOfEntryName(String pointOfEntryName) {
    this.pointOfEntryName = pointOfEntryName;
  }

  public CaseIndexDto surveillanceOfficerUuid(String surveillanceOfficerUuid) {
    this.surveillanceOfficerUuid = surveillanceOfficerUuid;
    return this;
  }

   /**
   * Get surveillanceOfficerUuid
   * @return surveillanceOfficerUuid
  **/
  @Schema(description = "")
  public String getSurveillanceOfficerUuid() {
    return surveillanceOfficerUuid;
  }

  public void setSurveillanceOfficerUuid(String surveillanceOfficerUuid) {
    this.surveillanceOfficerUuid = surveillanceOfficerUuid;
  }

  public CaseIndexDto outcome(CaseOutcome outcome) {
    this.outcome = outcome;
    return this;
  }

   /**
   * Get outcome
   * @return outcome
  **/
  @Schema(description = "")
  public CaseOutcome getOutcome() {
    return outcome;
  }

  public void setOutcome(CaseOutcome outcome) {
    this.outcome = outcome;
  }

  public CaseIndexDto sex(Sex sex) {
    this.sex = sex;
    return this;
  }

   /**
   * Get sex
   * @return sex
  **/
  @Schema(description = "")
  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public CaseIndexDto ageAndBirthDate(AgeAndBirthDateDto ageAndBirthDate) {
    this.ageAndBirthDate = ageAndBirthDate;
    return this;
  }

   /**
   * Get ageAndBirthDate
   * @return ageAndBirthDate
  **/
  @Schema(description = "")
  public AgeAndBirthDateDto getAgeAndBirthDate() {
    return ageAndBirthDate;
  }

  public void setAgeAndBirthDate(AgeAndBirthDateDto ageAndBirthDate) {
    this.ageAndBirthDate = ageAndBirthDate;
  }

  public CaseIndexDto completeness(Float completeness) {
    this.completeness = completeness;
    return this;
  }

   /**
   * Get completeness
   * @return completeness
  **/
  @Schema(description = "")
  public Float getCompleteness() {
    return completeness;
  }

  public void setCompleteness(Float completeness) {
    this.completeness = completeness;
  }

  public CaseIndexDto quarantineTo(Instant quarantineTo) {
    this.quarantineTo = quarantineTo;
    return this;
  }

   /**
   * Get quarantineTo
   * @return quarantineTo
  **/
  @Schema(description = "")
  public Instant getQuarantineTo() {
    return quarantineTo;
  }

  public void setQuarantineTo(Instant quarantineTo) {
    this.quarantineTo = quarantineTo;
  }

  public CaseIndexDto followUpStatus(FollowUpStatus followUpStatus) {
    this.followUpStatus = followUpStatus;
    return this;
  }

   /**
   * Get followUpStatus
   * @return followUpStatus
  **/
  @Schema(description = "")
  public FollowUpStatus getFollowUpStatus() {
    return followUpStatus;
  }

  public void setFollowUpStatus(FollowUpStatus followUpStatus) {
    this.followUpStatus = followUpStatus;
  }

  public CaseIndexDto followUpUntil(Instant followUpUntil) {
    this.followUpUntil = followUpUntil;
    return this;
  }

   /**
   * Get followUpUntil
   * @return followUpUntil
  **/
  @Schema(description = "")
  public Instant getFollowUpUntil() {
    return followUpUntil;
  }

  public void setFollowUpUntil(Instant followUpUntil) {
    this.followUpUntil = followUpUntil;
  }

  public CaseIndexDto symptomJournalStatus(SymptomJournalStatus symptomJournalStatus) {
    this.symptomJournalStatus = symptomJournalStatus;
    return this;
  }

   /**
   * Get symptomJournalStatus
   * @return symptomJournalStatus
  **/
  @Schema(description = "")
  public SymptomJournalStatus getSymptomJournalStatus() {
    return symptomJournalStatus;
  }

  public void setSymptomJournalStatus(SymptomJournalStatus symptomJournalStatus) {
    this.symptomJournalStatus = symptomJournalStatus;
  }

  public CaseIndexDto vaccination(Vaccination vaccination) {
    this.vaccination = vaccination;
    return this;
  }

   /**
   * Get vaccination
   * @return vaccination
  **/
  @Schema(description = "")
  public Vaccination getVaccination() {
    return vaccination;
  }

  public void setVaccination(Vaccination vaccination) {
    this.vaccination = vaccination;
  }

  public CaseIndexDto visitCount(Integer visitCount) {
    this.visitCount = visitCount;
    return this;
  }

   /**
   * Get visitCount
   * @return visitCount
  **/
  @Schema(description = "")
  public Integer getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(Integer visitCount) {
    this.visitCount = visitCount;
  }

  public CaseIndexDto surveillanceToolLastShareDate(Instant surveillanceToolLastShareDate) {
    this.surveillanceToolLastShareDate = surveillanceToolLastShareDate;
    return this;
  }

   /**
   * Get surveillanceToolLastShareDate
   * @return surveillanceToolLastShareDate
  **/
  @Schema(description = "")
  public Instant getSurveillanceToolLastShareDate() {
    return surveillanceToolLastShareDate;
  }

  public void setSurveillanceToolLastShareDate(Instant surveillanceToolLastShareDate) {
    this.surveillanceToolLastShareDate = surveillanceToolLastShareDate;
  }

  public CaseIndexDto surveillanceToolShareCount(Long surveillanceToolShareCount) {
    this.surveillanceToolShareCount = surveillanceToolShareCount;
    return this;
  }

   /**
   * Get surveillanceToolShareCount
   * @return surveillanceToolShareCount
  **/
  @Schema(description = "")
  public Long getSurveillanceToolShareCount() {
    return surveillanceToolShareCount;
  }

  public void setSurveillanceToolShareCount(Long surveillanceToolShareCount) {
    this.surveillanceToolShareCount = surveillanceToolShareCount;
  }

  public CaseIndexDto surveillanceToolStatus(ExternalShareStatus surveillanceToolStatus) {
    this.surveillanceToolStatus = surveillanceToolStatus;
    return this;
  }

   /**
   * Get surveillanceToolStatus
   * @return surveillanceToolStatus
  **/
  @Schema(description = "")
  public ExternalShareStatus getSurveillanceToolStatus() {
    return surveillanceToolStatus;
  }

  public void setSurveillanceToolStatus(ExternalShareStatus surveillanceToolStatus) {
    this.surveillanceToolStatus = surveillanceToolStatus;
  }

  public CaseIndexDto jurisdiction(CaseJurisdictionDto jurisdiction) {
    this.jurisdiction = jurisdiction;
    return this;
  }

   /**
   * Get jurisdiction
   * @return jurisdiction
  **/
  @Schema(description = "")
  public CaseJurisdictionDto getJurisdiction() {
    return jurisdiction;
  }

  public void setJurisdiction(CaseJurisdictionDto jurisdiction) {
    this.jurisdiction = jurisdiction;
  }

  public CaseIndexDto responsibleRegionUuid(String responsibleRegionUuid) {
    this.responsibleRegionUuid = responsibleRegionUuid;
    return this;
  }

   /**
   * Get responsibleRegionUuid
   * @return responsibleRegionUuid
  **/
  @Schema(description = "")
  public String getResponsibleRegionUuid() {
    return responsibleRegionUuid;
  }

  public void setResponsibleRegionUuid(String responsibleRegionUuid) {
    this.responsibleRegionUuid = responsibleRegionUuid;
  }

  public CaseIndexDto regionUuid(String regionUuid) {
    this.regionUuid = regionUuid;
    return this;
  }

   /**
   * Get regionUuid
   * @return regionUuid
  **/
  @Schema(description = "")
  public String getRegionUuid() {
    return regionUuid;
  }

  public void setRegionUuid(String regionUuid) {
    this.regionUuid = regionUuid;
  }

  public CaseIndexDto responsibleDistrictUuid(String responsibleDistrictUuid) {
    this.responsibleDistrictUuid = responsibleDistrictUuid;
    return this;
  }

   /**
   * Get responsibleDistrictUuid
   * @return responsibleDistrictUuid
  **/
  @Schema(description = "")
  public String getResponsibleDistrictUuid() {
    return responsibleDistrictUuid;
  }

  public void setResponsibleDistrictUuid(String responsibleDistrictUuid) {
    this.responsibleDistrictUuid = responsibleDistrictUuid;
  }

  public CaseIndexDto districtUuid(String districtUuid) {
    this.districtUuid = districtUuid;
    return this;
  }

   /**
   * Get districtUuid
   * @return districtUuid
  **/
  @Schema(description = "")
  public String getDistrictUuid() {
    return districtUuid;
  }

  public void setDistrictUuid(String districtUuid) {
    this.districtUuid = districtUuid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaseIndexDto caseIndexDto = (CaseIndexDto) o;
    return Objects.equals(this.pseudonymized, caseIndexDto.pseudonymized) &&
        Objects.equals(this.id, caseIndexDto.id) &&
        Objects.equals(this.uuid, caseIndexDto.uuid) &&
        Objects.equals(this.epidNumber, caseIndexDto.epidNumber) &&
        Objects.equals(this.externalID, caseIndexDto.externalID) &&
        Objects.equals(this.externalToken, caseIndexDto.externalToken) &&
        Objects.equals(this.personFirstName, caseIndexDto.personFirstName) &&
        Objects.equals(this.personLastName, caseIndexDto.personLastName) &&
        Objects.equals(this.disease, caseIndexDto.disease) &&
        Objects.equals(this.diseaseVariant, caseIndexDto.diseaseVariant) &&
        Objects.equals(this.diseaseDetails, caseIndexDto.diseaseDetails) &&
        Objects.equals(this.caseClassification, caseIndexDto.caseClassification) &&
        Objects.equals(this.investigationStatus, caseIndexDto.investigationStatus) &&
        Objects.equals(this.presentCondition, caseIndexDto.presentCondition) &&
        Objects.equals(this.reportDate, caseIndexDto.reportDate) &&
        Objects.equals(this.creationDate, caseIndexDto.creationDate) &&
        Objects.equals(this.districtName, caseIndexDto.districtName) &&
        Objects.equals(this.healthFacilityName, caseIndexDto.healthFacilityName) &&
        Objects.equals(this.pointOfEntryName, caseIndexDto.pointOfEntryName) &&
        Objects.equals(this.surveillanceOfficerUuid, caseIndexDto.surveillanceOfficerUuid) &&
        Objects.equals(this.outcome, caseIndexDto.outcome) &&
        Objects.equals(this.sex, caseIndexDto.sex) &&
        Objects.equals(this.ageAndBirthDate, caseIndexDto.ageAndBirthDate) &&
        Objects.equals(this.completeness, caseIndexDto.completeness) &&
        Objects.equals(this.quarantineTo, caseIndexDto.quarantineTo) &&
        Objects.equals(this.followUpStatus, caseIndexDto.followUpStatus) &&
        Objects.equals(this.followUpUntil, caseIndexDto.followUpUntil) &&
        Objects.equals(this.symptomJournalStatus, caseIndexDto.symptomJournalStatus) &&
        Objects.equals(this.vaccination, caseIndexDto.vaccination) &&
        Objects.equals(this.visitCount, caseIndexDto.visitCount) &&
        Objects.equals(this.surveillanceToolLastShareDate, caseIndexDto.surveillanceToolLastShareDate) &&
        Objects.equals(this.surveillanceToolShareCount, caseIndexDto.surveillanceToolShareCount) &&
        Objects.equals(this.surveillanceToolStatus, caseIndexDto.surveillanceToolStatus) &&
        Objects.equals(this.jurisdiction, caseIndexDto.jurisdiction) &&
        Objects.equals(this.responsibleRegionUuid, caseIndexDto.responsibleRegionUuid) &&
        Objects.equals(this.regionUuid, caseIndexDto.regionUuid) &&
        Objects.equals(this.responsibleDistrictUuid, caseIndexDto.responsibleDistrictUuid) &&
        Objects.equals(this.districtUuid, caseIndexDto.districtUuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pseudonymized, id, uuid, epidNumber, externalID, externalToken, personFirstName, personLastName, disease, diseaseVariant, diseaseDetails, caseClassification, investigationStatus, presentCondition, reportDate, creationDate, districtName, healthFacilityName, pointOfEntryName, surveillanceOfficerUuid, outcome, sex, ageAndBirthDate, completeness, quarantineTo, followUpStatus, followUpUntil, symptomJournalStatus, vaccination, visitCount, surveillanceToolLastShareDate, surveillanceToolShareCount, surveillanceToolStatus, jurisdiction, responsibleRegionUuid, regionUuid, responsibleDistrictUuid, districtUuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaseIndexDto {\n");
    
    sb.append("    pseudonymized: ").append(toIndentedString(pseudonymized)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    epidNumber: ").append(toIndentedString(epidNumber)).append("\n");
    sb.append("    externalID: ").append(toIndentedString(externalID)).append("\n");
    sb.append("    externalToken: ").append(toIndentedString(externalToken)).append("\n");
    sb.append("    personFirstName: ").append(toIndentedString(personFirstName)).append("\n");
    sb.append("    personLastName: ").append(toIndentedString(personLastName)).append("\n");
    sb.append("    disease: ").append(toIndentedString(disease)).append("\n");
    sb.append("    diseaseVariant: ").append(toIndentedString(diseaseVariant)).append("\n");
    sb.append("    diseaseDetails: ").append(toIndentedString(diseaseDetails)).append("\n");
    sb.append("    caseClassification: ").append(toIndentedString(caseClassification)).append("\n");
    sb.append("    investigationStatus: ").append(toIndentedString(investigationStatus)).append("\n");
    sb.append("    presentCondition: ").append(toIndentedString(presentCondition)).append("\n");
    sb.append("    reportDate: ").append(toIndentedString(reportDate)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    healthFacilityName: ").append(toIndentedString(healthFacilityName)).append("\n");
    sb.append("    pointOfEntryName: ").append(toIndentedString(pointOfEntryName)).append("\n");
    sb.append("    surveillanceOfficerUuid: ").append(toIndentedString(surveillanceOfficerUuid)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    sex: ").append(toIndentedString(sex)).append("\n");
    sb.append("    ageAndBirthDate: ").append(toIndentedString(ageAndBirthDate)).append("\n");
    sb.append("    completeness: ").append(toIndentedString(completeness)).append("\n");
    sb.append("    quarantineTo: ").append(toIndentedString(quarantineTo)).append("\n");
    sb.append("    followUpStatus: ").append(toIndentedString(followUpStatus)).append("\n");
    sb.append("    followUpUntil: ").append(toIndentedString(followUpUntil)).append("\n");
    sb.append("    symptomJournalStatus: ").append(toIndentedString(symptomJournalStatus)).append("\n");
    sb.append("    vaccination: ").append(toIndentedString(vaccination)).append("\n");
    sb.append("    visitCount: ").append(toIndentedString(visitCount)).append("\n");
    sb.append("    surveillanceToolLastShareDate: ").append(toIndentedString(surveillanceToolLastShareDate)).append("\n");
    sb.append("    surveillanceToolShareCount: ").append(toIndentedString(surveillanceToolShareCount)).append("\n");
    sb.append("    surveillanceToolStatus: ").append(toIndentedString(surveillanceToolStatus)).append("\n");
    sb.append("    jurisdiction: ").append(toIndentedString(jurisdiction)).append("\n");
    sb.append("    responsibleRegionUuid: ").append(toIndentedString(responsibleRegionUuid)).append("\n");
    sb.append("    regionUuid: ").append(toIndentedString(regionUuid)).append("\n");
    sb.append("    responsibleDistrictUuid: ").append(toIndentedString(responsibleDistrictUuid)).append("\n");
    sb.append("    districtUuid: ").append(toIndentedString(districtUuid)).append("\n");
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
