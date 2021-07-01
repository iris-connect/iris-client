package iris.client_bff.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vavr.control.Try;
import iris.client_bff.core.mail.EmailSender;
import iris.client_bff.core.mail.EmailTemplates;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
public class EventEmailProviderTest {

	EventEmailProvider systemUnderTest;

	@Mock
	EmailSender emailSender;

	@Mock
	MessageSourceAccessor messageSourceAccessor;

	@BeforeEach
	void setUp() {
		systemUnderTest = new EventEmailProvider(emailSender, messageSourceAccessor);
	}

	@Test
	void sendDataRecievedEmailWithCorrectData() {
		String subject = "Neue Event Daten sind verfügbar auf dem IRIS Portal";
		String eventName = "EventName";
		String eventRefId = "RefId";
		Instant eventStart = Instant.now();
		Instant eventEnd = Instant.now();

		EventDataRequest eventData =
			EventDataRequest.builder().name(eventName).refId(eventRefId).requestStart(eventStart).requestEnd(eventEnd).build();

		when(messageSourceAccessor.getMessage("EventDataRecievedEmail.subject")).thenReturn(subject);

		when(emailSender.sendMail(any())).thenReturn(Try.success(null));

		systemUnderTest.sendDataRecievedEmail(eventData);

		ArgumentCaptor<EventEmail> argument = ArgumentCaptor.forClass(EventEmail.class);
		verify(emailSender, times(1)).sendMail(any());
		verify(emailSender).sendMail(argument.capture());

		assertEquals(subject, argument.getValue().getSubject());
		assertEquals(eventData.getName(), argument.getValue().getPlaceholders().get("eventId"));
		assertEquals(eventData.getRefId(), argument.getValue().getPlaceholders().get("externalId"));
		assertEquals(eventData.getRequestStart(), argument.getValue().getPlaceholders().get("startTime"));
		assertEquals(eventData.getRequestEnd(), argument.getValue().getPlaceholders().get("endTime"));
		assertTrue(argument.getValue().getPlaceholders().get("eventUrl").toString().contains(eventData.getId().toString()));
		assertEquals(EmailTemplates.Keys.EVENT_DATA_RECIEVED_MAIL_FTLH, argument.getValue().getTemplate());
	}
}
