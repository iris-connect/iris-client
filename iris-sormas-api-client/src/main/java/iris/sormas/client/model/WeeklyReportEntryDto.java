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
import iris.sormas.client.model.Disease;
import java.time.OffsetDateTime;
import java.time.Instant;
/**
 * WeeklyReportEntryDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class WeeklyReportEntryDto {
  @JsonProperty("creationDate")
  private Instant creationDate = null;

  @JsonProperty("changeDate")
  private Instant changeDate = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("disease")
  private Disease disease = null;

  @JsonProperty("numberOfCases")
  private Integer numberOfCases = null;

  public WeeklyReportEntryDto creationDate(Instant creationDate) {
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

  public WeeklyReportEntryDto changeDate(Instant changeDate) {
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

  public WeeklyReportEntryDto uuid(String uuid) {
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

  public WeeklyReportEntryDto disease(Disease disease) {
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

  public WeeklyReportEntryDto numberOfCases(Integer numberOfCases) {
    this.numberOfCases = numberOfCases;
    return this;
  }

   /**
   * Get numberOfCases
   * @return numberOfCases
  **/
  @Schema(description = "")
  public Integer getNumberOfCases() {
    return numberOfCases;
  }

  public void setNumberOfCases(Integer numberOfCases) {
    this.numberOfCases = numberOfCases;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeeklyReportEntryDto weeklyReportEntryDto = (WeeklyReportEntryDto) o;
    return Objects.equals(this.creationDate, weeklyReportEntryDto.creationDate) &&
        Objects.equals(this.changeDate, weeklyReportEntryDto.changeDate) &&
        Objects.equals(this.uuid, weeklyReportEntryDto.uuid) &&
        Objects.equals(this.disease, weeklyReportEntryDto.disease) &&
        Objects.equals(this.numberOfCases, weeklyReportEntryDto.numberOfCases);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, changeDate, uuid, disease, numberOfCases);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeeklyReportEntryDto {\n");
    
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    disease: ").append(toIndentedString(disease)).append("\n");
    sb.append("    numberOfCases: ").append(toIndentedString(numberOfCases)).append("\n");
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
