package de.healthIMIS.iris.client.data_submission.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * GuestListDataProvider
 */
@Valid
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-18T08:11:24.698Z[GMT]")

public class GuestListDataProvider {

	@JsonProperty("name")
	private String name = null;

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
	@Schema(required = true, description = "Name of Location, Institution or Organizer")
	@NotNull

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	@Schema(required = true, description = "")
	@NotNull

	@Valid
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
		return Objects.equals(this.name, guestListDataProvider.name) && Objects.equals(this.address, guestListDataProvider.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, address);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GuestListDataProvider {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
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
