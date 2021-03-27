package de.healthIMIS.iris.dummy_web.web.submissions;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Additional informations about the work place of the contact person.
 */
public class ContactPersonWorkPlace {

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("pointOfContact")
	private String pointOfContact = null;

	@JsonProperty("phone")
	private String phone = null;

	@JsonProperty("address")
	private Address address = null;

	public ContactPersonWorkPlace name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of work place
	 * 
	 * @return name
	 **/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContactPersonWorkPlace pointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
		return this;
	}

	/**
	 * Get pointOfContact
	 * 
	 * @return pointOfContact
	 **/
	public String getPointOfContact() {
		return pointOfContact;
	}

	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}

	public ContactPersonWorkPlace phone(String phone) {
		this.phone = phone;
		return this;
	}

	/**
	 * Get phone
	 * 
	 * @return phone
	 **/
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ContactPersonWorkPlace address(Address address) {
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
		ContactPersonWorkPlace contactPersonWorkPlace = (ContactPersonWorkPlace) o;
		return Objects.equals(this.name, contactPersonWorkPlace.name)
				&& Objects.equals(this.pointOfContact, contactPersonWorkPlace.pointOfContact)
				&& Objects.equals(this.phone, contactPersonWorkPlace.phone)
				&& Objects.equals(this.address, contactPersonWorkPlace.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, pointOfContact, phone, address);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContactPersonWorkPlace {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    pointOfContact: ").append(toIndentedString(pointOfContact)).append("\n");
		sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
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
