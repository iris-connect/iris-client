package iris.client_bff.cases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.vavr.control.Try;
import iris.client_bff.core.IdentifierToken;
import iris.client_bff.core.mail.EmailSenderReal;
import iris.client_bff.core.mail.EmailTemplates;

import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
public class CaseEmailProviderTest {

	CaseEmailProvider systemUnderTest;

	@Mock
	EmailSenderReal emailSender;

	@Mock
	MessageSourceAccessor messageSourceAccessor;

	@Mock
	MailProperties mailProperties;

	@BeforeEach
	void setUp() {
		systemUnderTest = new CaseEmailProvider(emailSender, messageSourceAccessor, mailProperties);
		systemUnderTest.setMailingActive(true);
		systemUnderTest.setDataReceivedRecipientEmail("test@test.de");
	}

	@Test
	void sendDataReceivedEmailWithCorrectData() {
		String subject = "Neue Index Case Daten sind verfügbar auf dem IRIS Portal";
		CaseDataRequest caseData = createRequest();

		when(messageSourceAccessor.getMessage("CaseDataReceivedEmail.subject")).thenReturn(subject);

		when(emailSender.sendMail(any())).thenReturn(Try.success(null));

		systemUnderTest.sendDataReceivedEmailAsynchronously(caseData);

		ArgumentCaptor<CaseEmail> argument = ArgumentCaptor.forClass(CaseEmail.class);
		verify(emailSender, times(1)).sendMail(any());
		verify(emailSender).sendMail(argument.capture());

		assertEquals(subject, argument.getValue().getSubject());
		assertEquals(caseData.getName(), argument.getValue().getPlaceholders().get("caseId"));
		assertEquals(caseData.getRefId(), argument.getValue().getPlaceholders().get("externalId"));
		assertEquals(caseData.getRequestStart(), argument.getValue().getPlaceholders().get("startTime"));
		assertEquals(caseData.getRequestEnd(), argument.getValue().getPlaceholders().get("endTime"));
		assertTrue(argument.getValue().getPlaceholders().get("caseUrl").toString().contains(caseData.getId().toString()));
		assertEquals(EmailTemplates.Keys.CASE_DATA_RECEIVED_MAIL_FTLH, argument.getValue().getTemplate());
	}

	@Test
	void sendDataReceivedEmailAndTriggerEmailSenderFailuresBeforeSuccess() {
		String subject = "Neue Index Case Daten sind verfügbar auf dem IRIS Portal";
		CaseDataRequest caseData = createRequest();

		when(messageSourceAccessor.getMessage("CaseDataReceivedEmail.subject")).thenReturn(subject);

		when(emailSender.sendMail(any())).thenReturn(Try.failure(new Exception()), Try.failure(new Exception()),
				Try.success(null));

		systemUnderTest.sendDataReceivedEmailAsynchronously(caseData);

		verify(emailSender, times(3)).sendMail(any());
	}

	@Test
	void sendDataReceivedEmailAndTriggerEmailSenderFailuresTillLimitIsReached() {
		String subject = "Neue Index Case Daten sind verfügbar auf dem IRIS Portal";
		CaseDataRequest caseData = createRequest();

		when(messageSourceAccessor.getMessage("CaseDataReceivedEmail.subject")).thenReturn(subject);

		when(emailSender.sendMail(any())).thenReturn(
				Try.failure(new Exception()),
				Try.failure(new Exception()),
				Try.failure(new Exception()),
				Try.failure(new Exception()),
				Try.failure(new Exception()),
				Try.failure(new Exception()));

		systemUnderTest.sendDataReceivedEmailAsynchronously(caseData);

		verify(emailSender, times(6)).sendMail(any());
		Assertions.assertThrows(Exception.class, null);
	}

	private CaseDataRequest createRequest() {

		String caseName = "CaseName";
		String caseExternalCaseId = "externalCaseId";
		Instant caseStart = Instant.now();
		Instant caseEnd = Instant.now();

		var token = IdentifierToken.builder()
				.readableToken("readableToken")
				.connectionAuthorizationToken("CAT")
				.dataAuthorizationToken("DAT")
				.build();

		CaseDataRequest caseData = CaseDataRequest.builder()
				.name(caseName)
				.refId(caseExternalCaseId)
				.requestStart(caseStart)
				.requestEnd(caseEnd)
				.identifierToken(token)
				.build();

		return caseData;
	}
}
