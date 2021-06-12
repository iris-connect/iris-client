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
import iris.sormas.client.model.AreaType;
import iris.sormas.client.model.CommunityReferenceDto;
import iris.sormas.client.model.DistrictReferenceDto;
import iris.sormas.client.model.FacilityType;
import iris.sormas.client.model.RegionReferenceDto;
import java.time.OffsetDateTime;
import java.time.Instant;
/**
 * FacilityDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class FacilityDto {
  @JsonProperty("creationDate")
  private Instant creationDate = null;

  @JsonProperty("changeDate")
  private Instant changeDate = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("region")
  private RegionReferenceDto region = null;

  @JsonProperty("district")
  private DistrictReferenceDto district = null;

  @JsonProperty("community")
  private CommunityReferenceDto community = null;

  @JsonProperty("city")
  private String city = null;

  @JsonProperty("postalCode")
  private String postalCode = null;

  @JsonProperty("street")
  private String street = null;

  @JsonProperty("houseNumber")
  private String houseNumber = null;

  @JsonProperty("additionalInformation")
  private String additionalInformation = null;

  @JsonProperty("areaType")
  private AreaType areaType = null;

  @JsonProperty("contactPersonFirstName")
  private String contactPersonFirstName = null;

  @JsonProperty("contactPersonLastName")
  private String contactPersonLastName = null;

  @JsonProperty("contactPersonPhone")
  private String contactPersonPhone = null;

  @JsonProperty("contactPersonEmail")
  private String contactPersonEmail = null;

  @JsonProperty("latitude")
  private Double latitude = null;

  @JsonProperty("longitude")
  private Double longitude = null;

  @JsonProperty("type")
  private FacilityType type = null;

  @JsonProperty("publicOwnership")
  private Boolean publicOwnership = null;

  @JsonProperty("archived")
  private Boolean archived = null;

  @JsonProperty("externalID")
  private String externalID = null;

  public FacilityDto creationDate(Instant creationDate) {
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

  public FacilityDto changeDate(Instant changeDate) {
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

  public FacilityDto uuid(String uuid) {
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

  public FacilityDto name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FacilityDto region(RegionReferenceDto region) {
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

  public FacilityDto district(DistrictReferenceDto district) {
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

  public FacilityDto community(CommunityReferenceDto community) {
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

  public FacilityDto city(String city) {
    this.city = city;
    return this;
  }

   /**
   * Get city
   * @return city
  **/
  @Schema(description = "")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public FacilityDto postalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

   /**
   * Get postalCode
   * @return postalCode
  **/
  @Schema(description = "")
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public FacilityDto street(String street) {
    this.street = street;
    return this;
  }

   /**
   * Get street
   * @return street
  **/
  @Schema(description = "")
  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public FacilityDto houseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
    return this;
  }

   /**
   * Get houseNumber
   * @return houseNumber
  **/
  @Schema(description = "")
  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public FacilityDto additionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
    return this;
  }

   /**
   * Get additionalInformation
   * @return additionalInformation
  **/
  @Schema(description = "")
  public String getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public FacilityDto areaType(AreaType areaType) {
    this.areaType = areaType;
    return this;
  }

   /**
   * Get areaType
   * @return areaType
  **/
  @Schema(description = "")
  public AreaType getAreaType() {
    return areaType;
  }

  public void setAreaType(AreaType areaType) {
    this.areaType = areaType;
  }

  public FacilityDto contactPersonFirstName(String contactPersonFirstName) {
    this.contactPersonFirstName = contactPersonFirstName;
    return this;
  }

   /**
   * Get contactPersonFirstName
   * @return contactPersonFirstName
  **/
  @Schema(description = "")
  public String getContactPersonFirstName() {
    return contactPersonFirstName;
  }

  public void setContactPersonFirstName(String contactPersonFirstName) {
    this.contactPersonFirstName = contactPersonFirstName;
  }

  public FacilityDto contactPersonLastName(String contactPersonLastName) {
    this.contactPersonLastName = contactPersonLastName;
    return this;
  }

   /**
   * Get contactPersonLastName
   * @return contactPersonLastName
  **/
  @Schema(description = "")
  public String getContactPersonLastName() {
    return contactPersonLastName;
  }

  public void setContactPersonLastName(String contactPersonLastName) {
    this.contactPersonLastName = contactPersonLastName;
  }

  public FacilityDto contactPersonPhone(String contactPersonPhone) {
    this.contactPersonPhone = contactPersonPhone;
    return this;
  }

   /**
   * Get contactPersonPhone
   * @return contactPersonPhone
  **/
  @Schema(description = "")
  public String getContactPersonPhone() {
    return contactPersonPhone;
  }

  public void setContactPersonPhone(String contactPersonPhone) {
    this.contactPersonPhone = contactPersonPhone;
  }

  public FacilityDto contactPersonEmail(String contactPersonEmail) {
    this.contactPersonEmail = contactPersonEmail;
    return this;
  }

   /**
   * Get contactPersonEmail
   * @return contactPersonEmail
  **/
  @Schema(description = "")
  public String getContactPersonEmail() {
    return contactPersonEmail;
  }

  public void setContactPersonEmail(String contactPersonEmail) {
    this.contactPersonEmail = contactPersonEmail;
  }

  public FacilityDto latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * Get latitude
   * @return latitude
  **/
  @Schema(description = "")
  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public FacilityDto longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * Get longitude
   * @return longitude
  **/
  @Schema(description = "")
  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public FacilityDto type(FacilityType type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @Schema(description = "")
  public FacilityType getType() {
    return type;
  }

  public void setType(FacilityType type) {
    this.type = type;
  }

  public FacilityDto publicOwnership(Boolean publicOwnership) {
    this.publicOwnership = publicOwnership;
    return this;
  }

   /**
   * Get publicOwnership
   * @return publicOwnership
  **/
  @Schema(description = "")
  public Boolean isPublicOwnership() {
    return publicOwnership;
  }

  public void setPublicOwnership(Boolean publicOwnership) {
    this.publicOwnership = publicOwnership;
  }

  public FacilityDto archived(Boolean archived) {
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

  public FacilityDto externalID(String externalID) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FacilityDto facilityDto = (FacilityDto) o;
    return Objects.equals(this.creationDate, facilityDto.creationDate) &&
        Objects.equals(this.changeDate, facilityDto.changeDate) &&
        Objects.equals(this.uuid, facilityDto.uuid) &&
        Objects.equals(this.name, facilityDto.name) &&
        Objects.equals(this.region, facilityDto.region) &&
        Objects.equals(this.district, facilityDto.district) &&
        Objects.equals(this.community, facilityDto.community) &&
        Objects.equals(this.city, facilityDto.city) &&
        Objects.equals(this.postalCode, facilityDto.postalCode) &&
        Objects.equals(this.street, facilityDto.street) &&
        Objects.equals(this.houseNumber, facilityDto.houseNumber) &&
        Objects.equals(this.additionalInformation, facilityDto.additionalInformation) &&
        Objects.equals(this.areaType, facilityDto.areaType) &&
        Objects.equals(this.contactPersonFirstName, facilityDto.contactPersonFirstName) &&
        Objects.equals(this.contactPersonLastName, facilityDto.contactPersonLastName) &&
        Objects.equals(this.contactPersonPhone, facilityDto.contactPersonPhone) &&
        Objects.equals(this.contactPersonEmail, facilityDto.contactPersonEmail) &&
        Objects.equals(this.latitude, facilityDto.latitude) &&
        Objects.equals(this.longitude, facilityDto.longitude) &&
        Objects.equals(this.type, facilityDto.type) &&
        Objects.equals(this.publicOwnership, facilityDto.publicOwnership) &&
        Objects.equals(this.archived, facilityDto.archived) &&
        Objects.equals(this.externalID, facilityDto.externalID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, changeDate, uuid, name, region, district, community, city, postalCode, street, houseNumber, additionalInformation, areaType, contactPersonFirstName, contactPersonLastName, contactPersonPhone, contactPersonEmail, latitude, longitude, type, publicOwnership, archived, externalID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FacilityDto {\n");
    
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    district: ").append(toIndentedString(district)).append("\n");
    sb.append("    community: ").append(toIndentedString(community)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    houseNumber: ").append(toIndentedString(houseNumber)).append("\n");
    sb.append("    additionalInformation: ").append(toIndentedString(additionalInformation)).append("\n");
    sb.append("    areaType: ").append(toIndentedString(areaType)).append("\n");
    sb.append("    contactPersonFirstName: ").append(toIndentedString(contactPersonFirstName)).append("\n");
    sb.append("    contactPersonLastName: ").append(toIndentedString(contactPersonLastName)).append("\n");
    sb.append("    contactPersonPhone: ").append(toIndentedString(contactPersonPhone)).append("\n");
    sb.append("    contactPersonEmail: ").append(toIndentedString(contactPersonEmail)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    publicOwnership: ").append(toIndentedString(publicOwnership)).append("\n");
    sb.append("    archived: ").append(toIndentedString(archived)).append("\n");
    sb.append("    externalID: ").append(toIndentedString(externalID)).append("\n");
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