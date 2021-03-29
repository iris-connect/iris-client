package de.healthIMIS.iris.client.data_submission.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be
 * encoded with Base64!(&#x60;dataToTransport&#x60; in the general description of the API.)
 */
@Schema(
		description = "This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!(`dataToTransport` in the general description of the API.)")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
		date = "2021-03-26T10:26:53.535Z[GMT]")

public class ContactsAndEvents {
	@JsonProperty("contacts")
	private ContactPersonList contacts = null;

	@JsonProperty("events")
	private EventList events = null;

	@JsonProperty("dataProvider")
	private ContactsAndEventsDataProvider dataProvider = null;

	public ContactsAndEvents contacts(ContactPersonList contacts) {
		this.contacts = contacts;
		return this;
	}

	/**
	 * Get contacts
	 * 
	 * @return contacts
	 **/
	@Schema(required = true, description = "")
	@NotNull

	@Valid
	public ContactPersonList getContacts() {
		return contacts;
	}

	public void setContacts(ContactPersonList contacts) {
		this.contacts = contacts;
	}

	public ContactsAndEvents events(EventList events) {
		this.events = events;
		return this;
	}

	/**
	 * Get events
	 * 
	 * @return events
	 **/
	@Schema(required = true, description = "")
	@NotNull

	@Valid
	public EventList getEvents() {
		return events;
	}

	public void setEvents(EventList events) {
		this.events = events;
	}

	public ContactsAndEvents dataProvider(ContactsAndEventsDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		return this;
	}

	/**
	 * Get dataProvider
	 * 
	 * @return dataProvider
	 **/
	@Schema(required = true, description = "")
	@NotNull

	@Valid
	public ContactsAndEventsDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(ContactsAndEventsDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContactsAndEvents contactsAndEvents = (ContactsAndEvents) o;
		return Objects.equals(this.contacts, contactsAndEvents.contacts) &&
				Objects.equals(this.events, contactsAndEvents.events) &&
				Objects.equals(this.dataProvider, contactsAndEvents.dataProvider);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contacts, events, dataProvider);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContactsAndEvents {\n");

		sb.append("    contacts: ").append(toIndentedString(contacts)).append("\n");
		sb.append("    events: ").append(toIndentedString(events)).append("\n");
		sb.append("    dataProvider: ").append(toIndentedString(dataProvider)).append("\n");
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
