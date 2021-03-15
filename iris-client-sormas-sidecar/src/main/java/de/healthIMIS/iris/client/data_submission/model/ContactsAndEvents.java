package de.healthIMIS.iris.client.data_submission.model;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ContactsAndEvents
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-03-15T08:55:58.116Z[GMT]")

public class ContactsAndEvents {

	@JsonProperty("contacts")
	private ContactPersonList contacts = null;

	@JsonProperty("events")
	private EventList events = null;

	public ContactsAndEvents contacts(ContactPersonList contacts) {
		this.contacts = contacts;
		return this;
	}

	/**
	 * Get contacts
	 * 
	 * @return contacts
	 **/
	@Schema(description = "")

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
	@Schema(description = "")

	@Valid
	public EventList getEvents() {
		return events;
	}

	public void setEvents(EventList events) {
		this.events = events;
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
		return Objects.equals(this.contacts, contactsAndEvents.contacts) && Objects.equals(this.events, contactsAndEvents.events);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contacts, events);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContactsAndEvents {\n");

		sb.append("    contacts: ").append(toIndentedString(contacts)).append("\n");
		sb.append("    events: ").append(toIndentedString(events)).append("\n");
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
