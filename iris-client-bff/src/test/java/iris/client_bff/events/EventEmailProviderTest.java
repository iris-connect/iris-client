package iris.client_bff.events;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import iris.client_bff.core.mail.EmailSender;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
		String eventId = "eventId";

		EventDataRequest eventData =
			EventDataRequest.builder().name(eventName).refId(eventRefId).requestStart(eventStart).requestEnd(eventEnd).build();

		when(messageSourceAccessor.getMessage("EventDataRecievedEmail.subject")).thenReturn(subject);

		//when(emailSender.sendMail(any()))

		systemUnderTest.sendDataRecievedEmail(eventData);

		verify(emailSender, times(1)).sendMail(any());

	}

}
