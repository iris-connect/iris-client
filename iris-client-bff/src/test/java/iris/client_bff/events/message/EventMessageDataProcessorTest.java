package iris.client_bff.events.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionRepository;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.message.dto.ExportSelectionDto;
import iris.client_bff.events.message.dto.ImportSelectionDto;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.web.dto.GuestList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.util.Streamable;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventMessageDataProcessorTest {

	EventMessageTestData eventMessageTestData;

	EventMessageDataProcessor messageDataProcessor;

	@Mock
	EventDataRequestService requestService;

	@Mock
	EventDataSubmissionService submissionService;

	@Mock
	EventDataSubmissionRepository submissionRepository;

	@Mock
	EventMessageDataBuilder dataBuilder;

	@Mock
	EventMessageDataPayloadDefuse payloadDefuse;

	@Mock
	Validator validator;

	@Mock
	MessageSourceAccessor messages;

	ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		this.eventMessageTestData = new EventMessageTestData();
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.messageDataProcessor = new EventMessageDataProcessor(
				this.requestService,
				this.submissionService,
				this.submissionRepository,
				this.dataBuilder,
				this.payloadDefuse,
				this.validator,
				this.messages,
				this.objectMapper
		);
	}

	@Test
	void validateExportSelection() {

		when(this.validator.validate(any(ExportSelectionDto.class))).thenReturn(Set.of());

		this.messageDataProcessor.validateExportSelection(this.eventMessageTestData.MOCK_EVENT_MESSAGE_EXPORT_SELECTION_STRING);

		verify(this.validator).validate(this.eventMessageTestData.MOCK_EVENT_MESSAGE_EXPORT_SELECTION);
	}

	@Test
	void validateImportSelection() {

		when(this.validator.validate(any(ImportSelectionDto.class))).thenReturn(Set.of());

		this.messageDataProcessor.validateImportSelection(this.eventMessageTestData.MOCK_EVENT_MESSAGE_IMPORT_SELECTION_STRING);

		verify(this.validator).validate(this.eventMessageTestData.MOCK_EVENT_MESSAGE_IMPORT_SELECTION);
	}

	@Test
	void buildPayload() {

		when(this.dataBuilder.buildPayload(any(ExportSelectionDto.class)))
				.thenReturn(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD);

		var result = this.messageDataProcessor.buildPayload(this.eventMessageTestData.MOCK_EVENT_MESSAGE_EXPORT_SELECTION_STRING);

		verify(this.dataBuilder).buildPayload(this.eventMessageTestData.MOCK_EVENT_MESSAGE_EXPORT_SELECTION);

		assertEquals(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD_STRING, result);
	}

	@Test
	void importPayloadAndAdd() {

		doNothing().when(this.payloadDefuse).defuse(any(EventMessageDataPayload.class));

		when(this.requestService.save(any(EventDataRequest.class))).thenReturn(this.eventMessageTestData.MOCK_EVENT_DATA_REQUEST);
		doNothing().when(this.submissionService).save(any(EventDataRequest.class), any(GuestList.class));

		this.messageDataProcessor.importPayload(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD_STRING);

		verify(this.payloadDefuse).defuse(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD);

		verify(this.requestService).save(any(EventDataRequest.class));
		verify(this.submissionService).save(any(EventDataRequest.class), any(GuestList.class));

	}

	@Test
	void importPayloadAndUpdate() {

		doNothing().when(this.payloadDefuse).defuse(any(EventMessageDataPayload.class));

		when(this.requestService.findById(any(UUID.class)))
				.thenReturn(Optional.of(this.eventMessageTestData.MOCK_EVENT_DATA_REQUEST));
		when(this.submissionRepository.findAllByRequest(any(EventDataRequest.class)))
				.thenReturn(Streamable.of(this.eventMessageTestData.MOCK_EVENT_DATA_SUBMISSION));
		when(this.submissionRepository.save(any(EventDataSubmission.class)))
				.thenReturn(this.eventMessageTestData.MOCK_EVENT_DATA_SUBMISSION);

		this.messageDataProcessor.importPayload(
				this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD_STRING,
				this.eventMessageTestData.MOCK_EVENT_DATA_REQUEST.getId().toUUID(),
				this.eventMessageTestData.MOCK_EVENT_MESSAGE_IMPORT_SELECTION_STRING
		);

		verify(this.payloadDefuse).defuse(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD);
		verify(this.requestService).findById(any(UUID.class));
		verify(this.submissionRepository).findAllByRequest(any(EventDataRequest.class));
		verify(this.submissionRepository).save(any(EventDataSubmission.class));

	}

	@Test
	void getViewPayload() {

		doNothing().when(this.payloadDefuse).defuse(any(EventMessageDataPayload.class));

		var result = this.messageDataProcessor.getViewPayload(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD_STRING);

		verify(this.payloadDefuse).defuse(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD);

		assertEquals(result, this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_VIEW_PAYLOAD);
	}

	@Test
	void getImportSelectionViewPayload() {

		doNothing().when(this.payloadDefuse).defuse(any(EventMessageDataPayload.class));

		when(this.requestService.findById(any(UUID.class)))
				.thenReturn(Optional.of(this.eventMessageTestData.MOCK_EVENT_DATA_REQUEST));
		when(this.submissionRepository.findAllByRequest(any(EventDataRequest.class)))
				.thenReturn(Streamable.of(this.eventMessageTestData.MOCK_EVENT_DATA_SUBMISSION));

		var result = this.messageDataProcessor.getImportSelectionViewPayload(
				this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD_STRING,
				this.eventMessageTestData.MOCK_EVENT_DATA_REQUEST.getId().toUUID()
		);

		verify(this.payloadDefuse).defuse(this.eventMessageTestData.MOCK_EVENT_MESSAGE_DATA_PAYLOAD);
		verify(this.requestService).findById(any(UUID.class));
		verify(this.submissionRepository).findAllByRequest(any(EventDataRequest.class));

		assertEquals(result, this.eventMessageTestData.MOCK_EVENT_MESSAGE_IMPORT_SELECTION_VIEW_PAYLOAD);
	}
}
