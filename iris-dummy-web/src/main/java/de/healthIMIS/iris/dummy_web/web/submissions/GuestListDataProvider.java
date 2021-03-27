package de.healthIMIS.iris.dummy_web.web.submissions;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GuestListDataProvider
 */

public class GuestListDataProvider {

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("facilityType")
	private FacilityType facilityType = null;

	@JsonProperty("address")
	private Address address = null;

	public GuestListDataProvider name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of Location, Institution or Organizer
	 * 
	 * @return name
	 **/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GuestListDataProvider facilityType(FacilityType facilityType) {
		this.facilityType = facilityType;
		return this;
	}

	/**
	 * Get facilityType
	 * 
	 * @return facilityType
	 **/
	public FacilityType getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(FacilityType facilityType) {
		this.facilityType = facilityType;
	}

	public GuestListDataProvider address(Address address) {
		this.address = address;
		return this;
	}

	/**
	 * Get address
	 * 
	 * @return address
	 **/
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GuestListDataProvider guestListDataProvider = (GuestListDataProvider) o;
		return Objects.equals(this.name, guestListDataProvider.name)
				&& Objects.equals(this.facilityType, guestListDataProvider.facilityType)
				&& Objects.equals(this.address, guestListDataProvider.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, facilityType, address);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GuestListDataProvider {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    facilityType: ").append(toIndentedString(facilityType)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
