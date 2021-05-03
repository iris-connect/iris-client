package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Anschrift des Standorts
 */
@ApiModel(description = "Anschrift des Standorts")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class LocationAddress   {
  @JsonProperty("street")
  private String street;

  @JsonProperty("city")
  private String city;

  @JsonProperty("zip")
  private String zip;

  public LocationAddress street(String street) {
    this.street = street;
    return this;
  }

  /**
   * street + number
   * @return street
  */
  @ApiModelProperty(example = "Türkenstr. 7", required = true, value = "street + number")
  @NotNull


  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public LocationAddress city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Stadt
   * @return city
  */
  @ApiModelProperty(example = "München", required = true, value = "Stadt")
  @NotNull


  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public LocationAddress zip(String zip) {
    this.zip = zip;
    return this;
  }

  /**
   * Postleitzahl
   * @return zip
  */
  @ApiModelProperty(example = "80333", required = true, value = "Postleitzahl")
  @NotNull


  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationAddress locationAddress = (LocationAddress) o;
    return Objects.equals(this.street, locationAddress.street) &&
        Objects.equals(this.city, locationAddress.city) &&
        Objects.equals(this.zip, locationAddress.zip);
  }

  @Override
  public int hashCode() {
    return Objects.hash(street, city, zip);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationAddress {\n");
    
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    zip: ").append(toIndentedString(zip)).append("\n");
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

