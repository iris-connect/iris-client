package de.healthIMIS.iris.client.data_submission.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extended person data type for contact persons who had contact with the queried person during the queried time.
 */
@Schema(
		description = "Extended person data type for contact persons who had contact with the queried person during the queried time.")
@Valid
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
		date = "2021-02-18T08:11:24.698Z[GMT]")

public class ContactPerson extends Person {

	@JsonProperty("workPlace")
	private ContactPersonWorkPlace workPlace = null;

	@JsonProperty("contactInformation")
	private ContactPersonContactInformation contactInformation = null;

	public ContactPerson workPlace(ContactPersonWorkPlace workPlace) {
		this.workPlace = workPlace;
		return this;
	}

	/**
	 * Get workPlace
	 * 
	 * @return workPlace
	 **/
	@Schema(description = "")

	@Valid
	public ContactPersonWorkPlace getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(ContactPersonWorkPlace workPlace) {
		this.workPlace = workPlace;
	}

	public ContactPerson contactInformation(ContactPersonContactInformation contactInformation) {
		this.contactInformation = contactInformation;
		return this;
	}

	/**
	 * Get contactInformation
	 * 
	 * @return contactInformation
	 **/
	@Schema(description = "")

	@Valid
	public ContactPersonContactInformation getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(ContactPersonContactInformation contactInformation) {
		this.contactInformation = contactInformation;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContactPerson contactPerson = (ContactPerson) o;
		return Objects.equals(this.workPlace, contactPerson.workPlace)
				&& Objects.equals(this.contactInformation, contactPerson.contactInformation) && super.equals(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(workPlace, contactInformation, super.hashCode());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContactPerson {\n");
		sb.append("    ").append(toIndentedString(super.toString())).append("\n");
		sb.append("    workPlace: ").append(toIndentedString(workPlace)).append("\n");
		sb.append("    contactInformation: ").append(toIndentedString(contactInformation)).append("\n");
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
