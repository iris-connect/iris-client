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
import iris.sormas.client.model.CommunityReferenceDto;
import iris.sormas.client.model.DistrictReferenceDto;
import iris.sormas.client.model.PersonAssociation;
import iris.sormas.client.model.PresentCondition;
import iris.sormas.client.model.RegionReferenceDto;
/**
 * PersonCriteria
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class PersonCriteria {
  @JsonProperty("birthdateYYYY")
  private Integer birthdateYYYY = null;

  @JsonProperty("birthdateMM")
  private Integer birthdateMM = null;

  @JsonProperty("birthdateDD")
  private Integer birthdateDD = null;

  @JsonProperty("nameAddressPhoneEmailLike")
  private String nameAddressPhoneEmailLike = null;

  @JsonProperty("presentCondition")
  private PresentCondition presentCondition = null;

  @JsonProperty("region")
  private RegionReferenceDto region = null;

  @JsonProperty("district")
  private DistrictReferenceDto district = null;

  @JsonProperty("community")
  private CommunityReferenceDto community = null;

  @JsonProperty("personAssociation")
  private PersonAssociation personAssociation = null;

  public PersonCriteria birthdateYYYY(Integer birthdateYYYY) {
    this.birthdateYYYY = birthdateYYYY;
    return this;
  }

   /**
   * Get birthdateYYYY
   * @return birthdateYYYY
  **/
  @Schema(description = "")
  public Integer getBirthdateYYYY() {
    return birthdateYYYY;
  }

  public void setBirthdateYYYY(Integer birthdateYYYY) {
    this.birthdateYYYY = birthdateYYYY;
  }

  public PersonCriteria birthdateMM(Integer birthdateMM) {
    this.birthdateMM = birthdateMM;
    return this;
  }

   /**
   * Get birthdateMM
   * @return birthdateMM
  **/
  @Schema(description = "")
  public Integer getBirthdateMM() {
    return birthdateMM;
  }

  public void setBirthdateMM(Integer birthdateMM) {
    this.birthdateMM = birthdateMM;
  }

  public PersonCriteria birthdateDD(Integer birthdateDD) {
    this.birthdateDD = birthdateDD;
    return this;
  }

   /**
   * Get birthdateDD
   * @return birthdateDD
  **/
  @Schema(description = "")
  public Integer getBirthdateDD() {
    return birthdateDD;
  }

  public void setBirthdateDD(Integer birthdateDD) {
    this.birthdateDD = birthdateDD;
  }

  public PersonCriteria nameAddressPhoneEmailLike(String nameAddressPhoneEmailLike) {
    this.nameAddressPhoneEmailLike = nameAddressPhoneEmailLike;
    return this;
  }

   /**
   * Get nameAddressPhoneEmailLike
   * @return nameAddressPhoneEmailLike
  **/
  @Schema(description = "")
  public String getNameAddressPhoneEmailLike() {
    return nameAddressPhoneEmailLike;
  }

  public void setNameAddressPhoneEmailLike(String nameAddressPhoneEmailLike) {
    this.nameAddressPhoneEmailLike = nameAddressPhoneEmailLike;
  }

  public PersonCriteria presentCondition(PresentCondition presentCondition) {
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

  public PersonCriteria region(RegionReferenceDto region) {
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

  public PersonCriteria district(DistrictReferenceDto district) {
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

  public PersonCriteria community(CommunityReferenceDto community) {
    this.community = community;
    return this;
  }

   /**
   * Get community
   * @return community
  **/
  @Schema(description = "")
  public CommunityReferenceDto getCommunity() {
    return community;
  }

  public void setCommunity(CommunityReferenceDto community) {
    this.community = community;
  }

  public PersonCriteria personAssociation(PersonAssociation personAssociation) {
    this.personAssociation = personAssociation;
    return this;
  }

   /**
   * Get personAssociation
   * @return personAssociation
  **/
  @Schema(description = "")
  public PersonAssociation getPersonAssociation() {
    return personAssociation;
  }

  public void setPersonAssociation(PersonAssociation personAssociation) {
    this.personAssociation = personAssociation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonCriteria personCriteria = (PersonCriteria) o;
    return Objects.equals(this.birthdateYYYY, personCriteria.birthdateYYYY) &&
        Objects.equals(this.birthdateMM, personCriteria.birthdateMM) &&
        Objects.equals(this.birthdateDD, personCriteria.birthdateDD) &&
        Objects.equals(this.nameAddressPhoneEmailLike, personCriteria.nameAddressPhoneEmailLike) &&
        Objects.equals(this.presentCondition, personCriteria.presentCondition) &&
        Objects.equals(this.region, personCriteria.region) &&
        Objects.equals(this.district, personCriteria.district) &&
        Objects.equals(this.community, personCriteria.community) &&
        Objects.equals(this.personAssociation, personCriteria.personAssociation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(birthdateYYYY, birthdateMM, birthdateDD, nameAddressPhoneEmailLike, presentCondition, region, district, community, personAssociation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonCriteria {\n");
    
    sb.append("    birthdateYYYY: ").append(toIndentedString(birthdateYYYY)).append("\n");
    sb.append("    birthdateMM: ").append(toIndentedString(birthdateMM)).append("\n");
    sb.append("    birthdateDD: ").append(toIndentedString(birthdateDD)).append("\n");
    sb.append("    nameAddressPhoneEmailLike: ").append(toIndentedString(nameAddressPhoneEmailLike)).append("\n");
    sb.append("    presentCondition: ").append(toIndentedString(presentCondition)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    district: ").append(toIndentedString(district)).append("\n");
    sb.append("    community: ").append(toIndentedString(community)).append("\n");
    sb.append("    personAssociation: ").append(toIndentedString(personAssociation)).append("\n");
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