package iris.client_bff.search_client.web.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Anschrift des Standorts
 */
@JsonPropertyOrder({
	LocationAddress.JSON_PROPERTY_STREET,
	LocationAddress.JSON_PROPERTY_CITY,
	LocationAddress.JSON_PROPERTY_ZIP
})
public class LocationAddress {
  public static final String JSON_PROPERTY_STREET = "street";
  private String street;

  public static final String JSON_PROPERTY_CITY = "city";
  private String city;

  public static final String JSON_PROPERTY_ZIP = "zip";
  private String zip;

  public LocationAddress street(String street) {

	this.street = street;
	return this;
  }

  /**
   * street + number
   * 
   * @return street
   **/
  @JsonProperty(JSON_PROPERTY_STREET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

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
   * 
   * @return city
   **/
  @JsonProperty(JSON_PROPERTY_CITY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

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
   * 
   * @return zip
   **/
  @JsonProperty(JSON_PROPERTY_ZIP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

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
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
	if (o == null) {
	  return "null";
	}
	return o.toString().replace("\n", "\n    ");
  }

}
