package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A collection of events visited by the queried person during the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!
 */
@ApiModel(description = "A collection of events visited by the queried person during the queried time. This data must be encrypted with the key of health department from DataRequest.keyOfHealthDepartment and must be encoded with Base64!")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class EventList   {
  @JsonProperty("events")
  @Valid
  private List<Event> events = new ArrayList<>();

  @JsonProperty("startDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @JsonProperty("endDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

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
   * @return events
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<Event> getEvents() {
    return events;
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }

  public EventList startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Start date of visits for this list.
   * @return startDate
  */
  @ApiModelProperty(value = "Start date of visits for this list.")

  @Valid

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
   * @return endDate
  */
  @ApiModelProperty(value = "End date of visits for this list.")

  @Valid

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventList eventList = (EventList) o;
    return Objects.equals(this.events, eventList.events) &&
        Objects.equals(this.startDate, eventList.startDate) &&
        Objects.equals(this.endDate, eventList.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(events, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventList {\n");
    
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

