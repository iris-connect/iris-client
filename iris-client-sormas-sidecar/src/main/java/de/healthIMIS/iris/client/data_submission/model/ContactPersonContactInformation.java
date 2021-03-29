package de.healthIMIS.iris.client.data_submission.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Additional informations about the contact(s) with the queried person.
 */
@Schema(description = "Additional informations about the contact(s) with the queried person.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
		date = "2021-03-26T10:26:53.535Z[GMT]")

public class ContactPersonContactInformation {
	@JsonProperty("firstContactDate")
	private LocalDate firstContactDate = null;

	@JsonProperty("lastContactDate")
	private LocalDate lastContactDate = null;

	@JsonProperty("contactCategory")
	private ContactCategory contactCategory = null;

	@JsonProperty("basicConditions")
	private String basicConditions = null;

	public ContactPersonContactInformation firstContactDate(LocalDate firstContactDate) {
		this.firstContactDate = firstContactDate;
		return this;
	}

	/**
	 * Get firstContactDate
	 * 
	 * @return firstContactDate
	 **/
	@Schema(description = "")

	@Valid
	public LocalDate getFirstContactDate() {
		return firstContactDate;
	}

	public void setFirstContactDate(LocalDate firstContactDate) {
		this.firstContactDate = firstContactDate;
	}

	public ContactPersonContactInformation lastContactDate(LocalDate lastContactDate) {
		this.lastContactDate = lastContactDate;
		return this;
	}

	/**
	 * Get lastContactDate
	 * 
	 * @return lastContactDate
	 **/
	@Schema(description = "")

	@Valid
	public LocalDate getLastContactDate() {
		return lastContactDate;
	}

	public void setLastContactDate(LocalDate lastContactDate) {
		this.lastContactDate = lastContactDate;
	}

	public ContactPersonContactInformation contactCategory(ContactCategory contactCategory) {
		this.contactCategory = contactCategory;
		return this;
	}

	/**
	 * Get contactCategory
	 * 
	 * @return contactCategory
	 **/
	@Schema(description = "")

	@Valid
	public ContactCategory getContactCategory() {
		return contactCategory;
	}

	public void setContactCategory(ContactCategory contactCategory) {
		this.contactCategory = contactCategory;
	}

	public ContactPersonContactInformation basicConditions(String basicConditions) {
		this.basicConditions = basicConditions;
		return this;
	}

	/**
	 * Informations about the basic conditions such as: from, to, place, inside|outside, mask yes|no, distance >=|< 1,5m,
	 * ventilated yes|no, remarks.
	 * 
	 * @return basicConditions
	 **/
	@Schema(
			description = "Informations about the basic conditions such as: from, to, place, inside|outside, mask yes|no, distance >=|< 1,5m, ventilated yes|no, remarks.")

	public String getBasicConditions() {
		return basicConditions;
	}

	public void setBasicConditions(String basicConditions) {
		this.basicConditions = basicConditions;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContactPersonContactInformation contactPersonContactInformation = (ContactPersonContactInformation) o;
		return Objects.equals(this.firstContactDate, contactPersonContactInformation.firstContactDate) &&
				Objects.equals(this.lastContactDate, contactPersonContactInformation.lastContactDate) &&
				Objects.equals(this.contactCategory, contactPersonContactInformation.contactCategory) &&
				Objects.equals(this.basicConditions, contactPersonContactInformation.basicConditions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstContactDate, lastContactDate, contactCategory, basicConditions);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContactPersonContactInformation {\n");

		sb.append("    firstContactDate: ").append(toIndentedString(firstContactDate)).append("\n");
		sb.append("    lastContactDate: ").append(toIndentedString(lastContactDate)).append("\n");
		sb.append("    contactCategory: ").append(toIndentedString(contactCategory)).append("\n");
		sb.append("    basicConditions: ").append(toIndentedString(basicConditions)).append("\n");
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
