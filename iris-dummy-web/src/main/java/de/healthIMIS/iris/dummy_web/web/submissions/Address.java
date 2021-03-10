package de.healthIMIS.iris.dummy_web.web.submissions;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Address
 */

public class Address {

	@JsonProperty("street")
	private String street = null;

	@JsonProperty("houseNumber")
	private String houseNumber = null;

	@JsonProperty("zipCode")
	private String zipCode = null;

	@JsonProperty("city")
	private String city = null;

	public Address street(String street) {
		this.street = street;
		return this;
	}

	/**
	 * Get street
	 * 
	 * @return street
	 **/
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Address houseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
		return this;
	}

	/**
	 * Get houseNumber
	 * 
	 * @return houseNumber
	 **/
	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public Address zipCode(String zipCode) {
		this.zipCode = zipCode;
		return this;
	}

	/**
	 * Get zipCode
	 * 
	 * @return zipCode
	 **/
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Address city(String city) {
		this.city = city;
		return this;
	}

	/**
	 * Get city
	 * 
	 * @return city
	 **/
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Address address = (Address) o;
		return Objects.equals(this.street, address.street)
			&& Objects.equals(this.houseNumber, address.houseNumber)
			&& Objects.equals(this.zipCode, address.zipCode)
			&& Objects.equals(this.city, address.city);
	}

	@Override
	public int hashCode() {
		return Objects.hash(street, houseNumber, zipCode, city);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Address {\n");

		sb.append("    street: ").append(toIndentedString(street)).append("\n");
		sb.append("    houseNumber: ").append(toIndentedString(houseNumber)).append("\n");
		sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
		sb.append("    city: ").append(toIndentedString(city)).append("\n");
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
