package iris.client_bff.events;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.vavr.control.Try;
import iris.client_bff.core.mail.EmailSenderReal;
import iris.client_bff.core.mail.EmailTemplates;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
class EventEmailProviderTest {

	EventEmailProvider systemUnderTest;

	@Mock
	EmailSenderReal emailSender;

	@Mock
	MessageSourceAccessor messageSourceAccessor;

	@Mock
	MailProperties mailProperties;

	@BeforeEach
	void setUp() {
		systemUnderTest = new EventEmailProvider(emailSender, messageSourceAccessor, mailProperties);
		systemUnderTest.setMailingActive(true);
		systemUnderTest.setDataReceivedRecipientEmail("test@test.de");
	}

	@Test
	void sendDataReceivedEmailWithCorrectData() {
		String subject = "Neue Event Daten sind verfügbar auf dem IRIS Portal";
		String eventName = "EventName";
		String eventRefId = "RefId";
		Instant eventStart = Instant.now();
		Instant eventEnd = Instant.now();

		EventDataRequest eventData = EventDataRequest.builder().name(eventName).refId(eventRefId).requestStart(eventStart)
				.requestEnd(eventEnd).build();

		when(messageSourceAccessor.getMessage("EventDataReceivedEmail.subject")).thenReturn(subject);

		when(emailSender.sendMail(any())).thenReturn(Try.success(null));

		systemUnderTest.sendDataReceivedEmailAsynchronously(eventData);

		ArgumentCaptor<EventEmail> argument = ArgumentCaptor.forClass(EventEmail.class);
		verify(emailSender, times(1)).sendMail(any());
		verify(emailSender).sendMail(argument.capture());

		var value = argument.getValue();
		var placeholders = value.getPlaceholders();

		assertEquals(subject, value.getSubject());
		assertEquals(eventData.getName(), placeholders.get("eventId"));
		assertEquals(eventData.getRefId(), placeholders.get("externalId"));
		assertEquals(Date.from(eventData.getRequestStart()), placeholders.get("startTime"));
		assertEquals(Date.from(eventData.getRequestEnd()), placeholders.get("endTime"));
		assertTrue(placeholders.get("eventUrl").toString().contains(eventData.getId().toString()));
		assertEquals(EmailTemplates.Keys.EVENT_DATA_RECEIVED_MAIL_FTLH, value.getTemplate());
	}
}
