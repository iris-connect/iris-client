package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.api.dto.PersonWithDefuseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A guest who attended a queried event or location in the queried time.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Guest extends PersonWithDefuseData {

	@JsonIgnore
	private String guestId;

	@Valid
	private GuestAttendanceInformation attendanceInformation;
	private Boolean identityChecked;

	private String messageDataSelectId;

	public String getMessageDataSelectId() {
		if (this.messageDataSelectId == null) {
			return this.guestId;
		}
		return this.messageDataSelectId;
	}

	public void setMessageDataSelectId(String messageDataSelectId) {
		this.messageDataSelectId = messageDataSelectId;
	}
}
