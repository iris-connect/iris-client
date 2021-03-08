package de.healthIMIS.iris.client.data_submission.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * GuestAttendanceInformation
 */
@Valid
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-18T08:11:24.698Z[GMT]")

public class GuestAttendanceInformation {

	@JsonProperty("descriptionOfParticipation")
	private String descriptionOfParticipation = null;

	@JsonProperty("attendFrom")
	private OffsetDateTime attendFrom = null;

	@JsonProperty("attendTo")
	private OffsetDateTime attendTo = null;

	@JsonProperty("additionalInformation")
	private String additionalInformation = null;

	public GuestAttendanceInformation descriptionOfParticipation(String descriptionOfParticipation) {
		this.descriptionOfParticipation = descriptionOfParticipation;
		return this;
	}

	/**
	 * Description of the type of participation.
	 * 
	 * @return descriptionOfParticipation
	 **/
	@Schema(example = "Guest or Staff", description = "Description of the type of participation.")

	public String getDescriptionOfParticipation() {
		return descriptionOfParticipation;
	}

	public void setDescriptionOfParticipation(String descriptionOfParticipation) {
		this.descriptionOfParticipation = descriptionOfParticipation;
	}

	public GuestAttendanceInformation attendFrom(OffsetDateTime attendFrom) {
		this.attendFrom = attendFrom;
		return this;
	}

	/**
	 * Start date/time of attendance of this guest.
	 * 
	 * @return attendFrom
	 **/
	@Schema(description = "Start date/time of attendance of this guest.")

	@Valid
	public OffsetDateTime getAttendFrom() {
		return attendFrom;
	}

	public void setAttendFrom(OffsetDateTime attendFrom) {
		this.attendFrom = attendFrom;
	}

	public GuestAttendanceInformation attendTo(OffsetDateTime attendTo) {
		this.attendTo = attendTo;
		return this;
	}

	/**
	 * End date/time of attendance of this guest.
	 * 
	 * @return attendTo
	 **/
	@Schema(description = "End date/time of attendance of this guest.")

	@Valid
	public OffsetDateTime getAttendTo() {
		return attendTo;
	}

	public void setAttendTo(OffsetDateTime attendTo) {
		this.attendTo = attendTo;
	}

	public GuestAttendanceInformation additionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
		return this;
	}

	/**
	 * Additional informations about the attendance.
	 * 
	 * @return additionalInformation
	 **/
	@Schema(example = "Table: 3; Floor: 2", description = "Additional informations about the attendance.")

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
		GuestAttendanceInformation guestAttendanceInformation = (GuestAttendanceInformation) o;
		return Objects.equals(this.descriptionOfParticipation, guestAttendanceInformation.descriptionOfParticipation)
			&& Objects.equals(this.attendFrom, guestAttendanceInformation.attendFrom)
			&& Objects.equals(this.attendTo, guestAttendanceInformation.attendTo)
			&& Objects.equals(this.additionalInformation, guestAttendanceInformation.additionalInformation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(descriptionOfParticipation, attendFrom, attendTo, additionalInformation);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GuestAttendanceInformation {\n");

		sb.append("    descriptionOfParticipation: ").append(toIndentedString(descriptionOfParticipation)).append("\n");
		sb.append("    attendFrom: ").append(toIndentedString(attendFrom)).append("\n");
		sb.append("    attendTo: ").append(toIndentedString(attendTo)).append("\n");
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
