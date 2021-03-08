package de.healthIMIS.iris.client.data_submission.model;

import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An event, location or occasion visited by the queried person during the queried time.
 */
@Schema(description = "An event, location or occasion visited by the queried person during the queried time.")
@Valid
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-18T08:11:24.698Z[GMT]")

public class Event {

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("phone")
	private String phone = null;

	@JsonProperty("address")
	private Address address = null;

	@JsonProperty("additionalInformation")
	private String additionalInformation = null;

	public Event name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of the event
	 * 
	 * @return name
	 **/
	@Schema(description = "Name of the event")

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Event phone(String phone) {
		this.phone = phone;
		return this;
	}

	/**
	 * Get phone
	 * 
	 * @return phone
	 **/
	@Schema(description = "")

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Event address(Address address) {
		this.address = address;
		return this;
	}

	/**
	 * Get address
	 * 
	 * @return address
	 **/
	@Schema(description = "")

	@Valid
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Event additionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
		return this;
	}

	/**
	 * Additional informations about the event.
	 * 
	 * @return additionalInformation
	 **/
	@Schema(description = "Additional informations about the event.")

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Event event = (Event) o;
		return Objects.equals(this.name, event.name)
			&& Objects.equals(this.phone, event.phone)
			&& Objects.equals(this.address, event.address)
			&& Objects.equals(this.additionalInformation, event.additionalInformation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, phone, address, additionalInformation);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Event {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    additionalInformation: ").append(toIndentedString(additionalInformation)).append("\n");
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
