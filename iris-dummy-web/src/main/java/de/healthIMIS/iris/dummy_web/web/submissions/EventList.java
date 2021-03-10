package de.healthIMIS.iris.dummy_web.web.submissions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A collection of events visited by the queried person during the queried time. This data must be encrypted with the key of health
 * department from DataRequest.keyOfHealthDepartment!
 */

public class EventList {

	@JsonProperty("events")
	private List<Event> events = new ArrayList<Event>();

	@JsonProperty("dataProvider")
	private ContactPersonListDataProvider dataProvider = null;

	@JsonProperty("startDate")
	private LocalDate startDate = null;

	@JsonProperty("endDate")
	private LocalDate endDate = null;

	public EventList events(List<Event> events) {
		this.events = events;
		return this;
	}

	public EventList addEventsItem(Event eventsItem) {
		this.events.add(eventsItem);
		return this;
	}

	/**
	 * Get events
	 * 
	 * @return events
	 **/
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public EventList dataProvider(ContactPersonListDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		return this;
	}

	/**
	 * Get dataProvider
	 * 
	 * @return dataProvider
	 **/
	public ContactPersonListDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(ContactPersonListDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public EventList startDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	/**
	 * Start date of visits for this list.
	 * 
	 * @return startDate
	 **/
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public EventList endDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	/**
	 * End date of visits for this list.
	 * 
	 * @return endDate
	 **/
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EventList eventList = (EventList) o;
		return Objects.equals(this.events, eventList.events)
			&& Objects.equals(this.dataProvider, eventList.dataProvider)
			&& Objects.equals(this.startDate, eventList.startDate)
			&& Objects.equals(this.endDate, eventList.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(events, dataProvider, startDate, endDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EventList {\n");

		sb.append("    events: ").append(toIndentedString(events)).append("\n");
		sb.append("    dataProvider: ").append(toIndentedString(dataProvider)).append("\n");
		sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
		sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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
