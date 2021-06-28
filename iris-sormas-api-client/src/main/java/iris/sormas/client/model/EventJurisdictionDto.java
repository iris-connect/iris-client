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
/**
 * EventJurisdictionDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class EventJurisdictionDto {
  @JsonProperty("reportingUserUuid")
  private String reportingUserUuid = null;

  @JsonProperty("responsibleUserUuid")
  private String responsibleUserUuid = null;

  @JsonProperty("regionUuid")
  private String regionUuid = null;

  @JsonProperty("districtUuid")
  private String districtUuid = null;

  @JsonProperty("communityUuid")
  private String communityUuid = null;

  public EventJurisdictionDto reportingUserUuid(String reportingUserUuid) {
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

  public EventJurisdictionDto responsibleUserUuid(String responsibleUserUuid) {
    this.responsibleUserUuid = responsibleUserUuid;
    return this;
  }

   /**
   * Get responsibleUserUuid
   * @return responsibleUserUuid
  **/
  @Schema(description = "")
  public String getResponsibleUserUuid() {
    return responsibleUserUuid;
  }

  public void setResponsibleUserUuid(String responsibleUserUuid) {
    this.responsibleUserUuid = responsibleUserUuid;
  }

  public EventJurisdictionDto regionUuid(String regionUuid) {
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

  public EventJurisdictionDto districtUuid(String districtUuid) {
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

  public EventJurisdictionDto communityUuid(String communityUuid) {
    this.communityUuid = communityUuid;
    return this;
  }

   /**
   * Get communityUuid
   * @return communityUuid
  **/
  @Schema(description = "")
  public String getCommunityUuid() {
    return communityUuid;
  }

  public void setCommunityUuid(String communityUuid) {
    this.communityUuid = communityUuid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventJurisdictionDto eventJurisdictionDto = (EventJurisdictionDto) o;
    return Objects.equals(this.reportingUserUuid, eventJurisdictionDto.reportingUserUuid) &&
        Objects.equals(this.responsibleUserUuid, eventJurisdictionDto.responsibleUserUuid) &&
        Objects.equals(this.regionUuid, eventJurisdictionDto.regionUuid) &&
        Objects.equals(this.districtUuid, eventJurisdictionDto.districtUuid) &&
        Objects.equals(this.communityUuid, eventJurisdictionDto.communityUuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportingUserUuid, responsibleUserUuid, regionUuid, districtUuid, communityUuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventJurisdictionDto {\n");
    
    sb.append("    reportingUserUuid: ").append(toIndentedString(reportingUserUuid)).append("\n");
    sb.append("    responsibleUserUuid: ").append(toIndentedString(responsibleUserUuid)).append("\n");
    sb.append("    regionUuid: ").append(toIndentedString(regionUuid)).append("\n");
    sb.append("    districtUuid: ").append(toIndentedString(districtUuid)).append("\n");
    sb.append("    communityUuid: ").append(toIndentedString(communityUuid)).append("\n");
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
