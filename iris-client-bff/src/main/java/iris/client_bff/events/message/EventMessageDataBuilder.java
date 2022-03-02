package iris.client_bff.events.message;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestRepository;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.message.dto.ExportSelectionDto;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
@AllArgsConstructor
public class EventMessageDataBuilder {

	private final EventDataRequestRepository requestRepository;
	private final EventDataSubmissionRepository submissionRepository;

	EventMessageDataPayload buildPayload(String exportSelection) throws IrisMessageDataException {
		ExportSelectionDto exportSelectionDto = ExportSelectionDto.toModel(exportSelection);
		EventDataRequest.DataRequestIdentifier eventId = EventDataRequest.DataRequestIdentifier
				.of(exportSelectionDto.getEvent());
		EventDataRequest eventDataRequest = this.getEventDataRequest(eventId);
		EventDataSubmission eventDataSubmission = this.getEventDataSubmission(eventId);
		return EventMessageDataPayload.fromModel(eventDataRequest, eventDataSubmission, exportSelectionDto.getGuests());
	}

	private EventDataRequest getEventDataRequest(EventDataRequest.DataRequestIdentifier eventId)
			throws IrisMessageDataException {
		Optional<EventDataRequest> optionalEventDataRequest = this.requestRepository.findById(eventId);
		if (optionalEventDataRequest.isEmpty()) {
			throw new IrisMessageDataException("iris_message.message_data_mismatch");
		}
		return optionalEventDataRequest.get();
	}

	private EventDataSubmission getEventDataSubmission(EventDataRequest.DataRequestIdentifier eventId)
			throws IrisMessageDataException {
		EventDataRequest eventDataRequest = this.getEventDataRequest(eventId);
		Optional<EventDataSubmission> optionalEventDataSubmission = submissionRepository
				.findAllByRequest(eventDataRequest).get().findFirst();
		if (optionalEventDataSubmission.isEmpty()) {
			throw new IrisMessageDataException("iris_message.message_data_mismatch");
		}
		return optionalEventDataSubmission.get();
	}

}
