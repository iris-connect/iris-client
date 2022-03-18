package iris.client_bff.events.message;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.events.eps.EventDataDefuse;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static iris.client_bff.ui.messages.ErrorMessages.INVALID_INPUT_STRING;

@Service
@RequiredArgsConstructor
public class EventMessageDataPayloadDefuse {

	private final ValidationHelper validationHelper;

	private final EventDataDefuse eventDataDefuse;

	void defuse(EventMessageDataPayload payload) {
		this.defuse(payload.getEventDataRequestPayload());
		this.defuse(payload.getEventDataSubmissionPayload());
	}

	void defuse(EventMessageDataPayload.EventDataRequestPayload payload) {
		payload
				.setName(this.defuse(payload.getName(), "name", 500));
	}

	void defuse(EventMessageDataPayload.EventDataSubmissionPayload payload) {
		try {
			this.eventDataDefuse.defuseGuestList(payload.getGuestList());
		} catch (Exception e) {
			throw new IrisMessageDataException(e);
		}
	}

	private String defuse(String input, String field, int maxLength) {
		if (input == null)
			return null;
		if (this.validationHelper.isPossibleAttack(input, field, true)) {
			return INVALID_INPUT_STRING;
		}
		return StringUtils.truncate(input, maxLength);
	}

}
