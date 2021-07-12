package iris.client_bff.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vavr.control.Try;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.core.mail.EmailSender;
import iris.client_bff.core.mail.EmailTemplates;

import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

@ExtendWith(MockitoExtension.class)
public class CaseEmailProviderTest {

	CaseEmailProvider systemUnderTest;

	@Mock
	EmailSender emailSender;

	@Mock
	MessageSourceAccessor messageSourceAccessor;

	@BeforeEach
	void setUp() {
		systemUnderTest = new CaseEmailProvider(emailSender, messageSourceAccessor);
	}

	@Test
	void sendDataReceivedEmailWithCorrectData() {
		String subject = "Neue Index Case Daten sind verfügbar auf dem IRIS Portal";
		String caseName = "CaseName";
		String caseExternalCaseId = "externalCaseId";
		String caseId = "caseId";
		Instant caseStart = Instant.now();
		Instant caseEnd = Instant.now();

		IndexCaseDetailsDTO caseData =
			IndexCaseDetailsDTO.builder().name(caseName).externalCaseId(caseExternalCaseId).start(caseStart).end(caseEnd).caseId(caseId).build();

		when(messageSourceAccessor.getMessage("CaseDataReceivedEmail.subject")).thenReturn(subject);

		when(emailSender.sendMail(any())).thenReturn(Try.success(null));

		systemUnderTest.sendDataReceivedEmailAsynchronously(caseData);

		ArgumentCaptor<CaseEmail> argument = ArgumentCaptor.forClass(CaseEmail.class);
		verify(emailSender, times(1)).sendMail(any());
		verify(emailSender).sendMail(argument.capture());

		assertEquals(subject, argument.getValue().getSubject());
		assertEquals(caseData.getName(), argument.getValue().getPlaceholders().get("caseId"));
		assertEquals(caseData.getExternalCaseId(), argument.getValue().getPlaceholders().get("externalId"));
		assertEquals(caseData.getStart(), argument.getValue().getPlaceholders().get("startTime"));
		assertEquals(caseData.getEnd(), argument.getValue().getPlaceholders().get("endTime"));
		assertTrue(argument.getValue().getPlaceholders().get("caseUrl").toString().contains(caseData.getCaseId().toString()));
		assertEquals(EmailTemplates.Keys.CASE_DATA_RECEIVED_MAIL_FTLH, argument.getValue().getTemplate());
	}

	@Test
	void sendDataReceivedEmailAndTriggerEmailSenderFailuresBeforeSuccess() {
		String subject = "Neue Index Case Daten sind verfügbar auf dem IRIS Portal";
		String caseName = "CaseName";
		String caseExternalCaseId = "externalCaseId";
		String caseId = "caseId";
		Instant caseStart = Instant.now();
		Instant caseEnd = Instant.now();

		IndexCaseDetailsDTO caseData =
			IndexCaseDetailsDTO.builder().name(caseName).externalCaseId(caseExternalCaseId).start(caseStart).end(caseEnd).caseId(caseId).build();

		when(messageSourceAccessor.getMessage("CaseDataReceivedEmail.subject")).thenReturn(subject);

		when(emailSender.sendMail(any())).thenReturn(Try.failure(new Exception()), Try.failure(new Exception()), Try.success(null));

		systemUnderTest.sendDataReceivedEmailAsynchronously(caseData);

		verify(emailSender, times(3)).sendMail(any());
	}

	@Test
	void sendDataReceivedEmailAndTriggerEmailSenderFailuresTillLimitIsReached() {
		String subject = "Neue Index Case Daten sind verfügbar auf dem IRIS Portal";
		String caseName = "CaseName";
		String caseExternalCaseId = "externalCaseId";
		String caseId = "caseId";
		Instant caseStart = Instant.now();
		Instant caseEnd = Instant.now();

		IndexCaseDetailsDTO caseData =
			IndexCaseDetailsDTO.builder().name(caseName).externalCaseId(caseExternalCaseId).start(caseStart).end(caseEnd).caseId(caseId).build();

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
}
