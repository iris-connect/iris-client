package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!(&#x60;dataToTransport&#x60; in the general description of the API.)
 */
@ApiModel(description = "This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!(`dataToTransport` in the general description of the API.)")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class ContactsAndEvents   {
  @JsonProperty("contacts")
  private ContactPersonList contacts;

  @JsonProperty("events")
  private EventList events;

  @JsonProperty("dataProvider")
  private ContactsAndEventsDataProvider dataProvider;

  public ContactsAndEvents contacts(ContactPersonList contacts) {
    this.contacts = contacts;
    return this;
  }

  /**
   * Get contacts
   * @return contacts
  */
  @ApiModelProperty(required = true, value = "")
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
   * @return events
  */
  @ApiModelProperty(required = true, value = "")
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
   * @return dataProvider
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public ContactsAndEventsDataProvider getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(ContactsAndEventsDataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }


  @Override
  public boolean equals(Object o) {
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
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

