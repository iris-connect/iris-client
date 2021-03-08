package de.healthIMIS.iris.client.data_submission.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Basic data type of a person.
 */
@Schema(description = "Basic data type of a person.")
@Valid
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-18T08:11:24.698Z[GMT]")

public class Person {

	@JsonProperty("firstName")
	private String firstName = null;

	@JsonProperty("lastName")
	private String lastName = null;

	@JsonProperty("dateOfBirth")
	private LocalDate dateOfBirth = null;

	@JsonProperty("sex")
	private Sex sex = null;

	@JsonProperty("email")
	private String email = null;

	@JsonProperty("phone")
	private String phone = null;

	@JsonProperty("mobilPhone")
	private String mobilPhone = null;

	@JsonProperty("address")
	private Address address = null;

	public Person firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * Get firstName
	 * 
	 * @return firstName
	 **/
	@Schema(required = true, description = "")
	@NotNull

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Person lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * Get lastName
	 * 
	 * @return lastName
	 **/
	@Schema(required = true, description = "")
	@NotNull

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Person dateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	/**
	 * Get dateOfBirth
	 * 
	 * @return dateOfBirth
	 **/
	@Schema(description = "")

	@Valid
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Person sex(Sex sex) {
		this.sex = sex;
		return this;
	}

	/**
	 * Get sex
	 * 
	 * @return sex
	 **/
	@Schema(description = "")

	@Valid
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Person email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Get email
	 * 
	 * @return email
	 **/
	@Schema(description = "")

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Person phone(String phone) {
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

	public Person mobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
		return this;
	}

	/**
	 * Get mobilPhone
	 * 
	 * @return mobilPhone
	 **/
	@Schema(description = "")

	public String getMobilPhone() {
		return mobilPhone;
	}

	public void setMobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
	}

	public Person address(Address address) {
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

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Person person = (Person) o;
		return Objects.equals(this.firstName, person.firstName)
			&& Objects.equals(this.lastName, person.lastName)
			&& Objects.equals(this.dateOfBirth, person.dateOfBirth)
			&& Objects.equals(this.sex, person.sex)
			&& Objects.equals(this.email, person.email)
			&& Objects.equals(this.phone, person.phone)
			&& Objects.equals(this.mobilPhone, person.mobilPhone)
			&& Objects.equals(this.address, person.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, dateOfBirth, sex, email, phone, mobilPhone, address);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Person {\n");

		sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
		sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
		sb.append("    dateOfBirth: ").append(toIndentedString(dateOfBirth)).append("\n");
		sb.append("    sex: ").append(toIndentedString(sex)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
		sb.append("    mobilPhone: ").append(toIndentedString(mobilPhone)).append("\n");
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
