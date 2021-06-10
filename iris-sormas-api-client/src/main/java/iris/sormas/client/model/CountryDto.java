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
import iris.sormas.client.model.SubcontinentReferenceDto;
import java.time.OffsetDateTime;
import java.time.Instant;
/**
 * CountryDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class CountryDto {
  @JsonProperty("creationDate")
  private Instant creationDate = null;

  @JsonProperty("changeDate")
  private Instant changeDate = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("defaultName")
  private String defaultName = null;

  @JsonProperty("externalId")
  private String externalId = null;

  @JsonProperty("isoCode")
  private String isoCode = null;

  @JsonProperty("unoCode")
  private String unoCode = null;

  @JsonProperty("archived")
  private Boolean archived = null;

  @JsonProperty("subcontinent")
  private SubcontinentReferenceDto subcontinent = null;

  public CountryDto creationDate(Instant creationDate) {
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

  public CountryDto changeDate(Instant changeDate) {
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

  public CountryDto uuid(String uuid) {
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

  public CountryDto defaultName(String defaultName) {
    this.defaultName = defaultName;
    return this;
  }

   /**
   * Get defaultName
   * @return defaultName
  **/
  @Schema(description = "")
  public String getDefaultName() {
    return defaultName;
  }

  public void setDefaultName(String defaultName) {
    this.defaultName = defaultName;
  }

  public CountryDto externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

   /**
   * Get externalId
   * @return externalId
  **/
  @Schema(description = "")
  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public CountryDto isoCode(String isoCode) {
    this.isoCode = isoCode;
    return this;
  }

   /**
   * Get isoCode
   * @return isoCode
  **/
  @Schema(description = "")
  public String getIsoCode() {
    return isoCode;
  }

  public void setIsoCode(String isoCode) {
    this.isoCode = isoCode;
  }

  public CountryDto unoCode(String unoCode) {
    this.unoCode = unoCode;
    return this;
  }

   /**
   * Get unoCode
   * @return unoCode
  **/
  @Schema(description = "")
  public String getUnoCode() {
    return unoCode;
  }

  public void setUnoCode(String unoCode) {
    this.unoCode = unoCode;
  }

  public CountryDto archived(Boolean archived) {
    this.archived = archived;
    return this;
  }

   /**
   * Get archived
   * @return archived
  **/
  @Schema(description = "")
  public Boolean isArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public CountryDto subcontinent(SubcontinentReferenceDto subcontinent) {
    this.subcontinent = subcontinent;
    return this;
  }

   /**
   * Get subcontinent
   * @return subcontinent
  **/
  @Schema(description = "")
  public SubcontinentReferenceDto getSubcontinent() {
    return subcontinent;
  }

  public void setSubcontinent(SubcontinentReferenceDto subcontinent) {
    this.subcontinent = subcontinent;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CountryDto countryDto = (CountryDto) o;
    return Objects.equals(this.creationDate, countryDto.creationDate) &&
        Objects.equals(this.changeDate, countryDto.changeDate) &&
        Objects.equals(this.uuid, countryDto.uuid) &&
        Objects.equals(this.defaultName, countryDto.defaultName) &&
        Objects.equals(this.externalId, countryDto.externalId) &&
        Objects.equals(this.isoCode, countryDto.isoCode) &&
        Objects.equals(this.unoCode, countryDto.unoCode) &&
        Objects.equals(this.archived, countryDto.archived) &&
        Objects.equals(this.subcontinent, countryDto.subcontinent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, changeDate, uuid, defaultName, externalId, isoCode, unoCode, archived, subcontinent);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CountryDto {\n");
    
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    defaultName: ").append(toIndentedString(defaultName)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    isoCode: ").append(toIndentedString(isoCode)).append("\n");
    sb.append("    unoCode: ").append(toIndentedString(unoCode)).append("\n");
    sb.append("    archived: ").append(toIndentedString(archived)).append("\n");
    sb.append("    subcontinent: ").append(toIndentedString(subcontinent)).append("\n");
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
