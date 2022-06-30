package iris.client_bff.core.mail;

import static org.assertj.core.api.Assertions.*;

import io.vavr.control.Try;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.mail.EmailSender.AbstractTemplatedEmail;
import iris.client_bff.core.mail.EmailSender.TemplatedEmail.ConfiguredRecipient;
import iris.client_bff.core.mail.EmailSender.TemplatedEmail.Recipient;
import iris.client_bff.core.mail.EmailTemplates.Key;
import iris.client_bff.core.model.EmailAddress;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Map;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.ActiveProfiles;

import com.icegreen.greenmail.util.GreenMailUtil;

@RequiredArgsConstructor
@IrisWebIntegrationTest
@ActiveProfiles({ "dev", "test" })
class EmailSenderIntegrationTests {

	final EmailSenderReal sender;
	final MessageSourceAccessor messages;
	final TestEmailServer mailServer;
	final MailProperties mailProperties;

	private TestEmails testEmails;

	@BeforeEach
	void setUp() throws Exception {
		mailServer.reset();

		testEmails = new TestEmails(sender, messages, mailProperties);
	}

	@Test
	void sendSimpleMailCorrect_fixSenderAndRecipient_En() throws MessagingException {

		testEmails.sendTestEmailEn();

		mailServer.assertEmailSent(message -> {

			assertThat(message.getSubject()).isEqualTo("This is a test mail");
			assertThat(GreenMailUtil.getBody(message)).startsWith("Hi Name");
			assertThat(message.getRecipients(RecipientType.TO)[0])
					.hasToString("\"Test Person - test {at} test.de\" <testmailbox@iris-gateway.de>");
			assertThat(message.getFrom()[0]).hasToString("Gesundheitsamt Entenhausen <mail-ga@entenhausen.de>");
		});
	}

	@Test
	void sendSimpleMailCorrect_fixSenderAndRecipient_De() throws MessagingException {

		testEmails.sendTestEmailDe();

		mailServer.assertEmailSent(message -> {

			assertThat(message.getContentType()).contains("text/plain");
			assertThat(message.getSubject()).isEqualTo("Das ist eine Test-Mail");
			assertThat(GreenMailUtil.getBody(message)).startsWith("Hallo Name");
			assertThat(message.getRecipients(RecipientType.TO)[0])
					.hasToString("\"Test Person - test {at} test.de\" <testmailbox@iris-gateway.de>");
			assertThat(message.getFrom()[0]).hasToString("Gesundheitsamt Entenhausen <mail-ga@entenhausen.de>");
		});
	}

	@Test
	void sendHtmlMailCorrect_fixSenderAndRecipient_De() throws MessagingException {

		testEmails.sendTestHtmlEmailDe();

		mailServer.assertEmailSent(message -> {

			assertThat(message.getContentType()).contains("multipart/mixed");
			assertThat(message.getSubject()).isEqualTo("Das ist eine Test-Mail");
			assertThat(GreenMailUtil.getBody(message)).contains("Hallo Name");
			assertThat(message.getRecipients(RecipientType.TO)[0])
					.hasToString("\"Test Person - test {at} test.de\" <testmailbox@iris-gateway.de>");
			assertThat(message.getFrom()[0]).hasToString("Gesundheitsamt Entenhausen <mail-ga@entenhausen.de>");
		});
	}

	public class TestEmails extends EmailProvider {

		public TestEmails(EmailSender emailSender, MessageSourceAccessor messages, MailProperties mailProperties) {
			super(emailSender, messages, mailProperties);
		}

		ConfiguredRecipient standardRecipient = new ConfiguredRecipient("fix.recipient",
				EmailAddress.of("fix-recipient@iris-connect.de"));

		Try<Void> sendTestEmailEn() {

			var english = Locale.ENGLISH;
			var subject = messages.getMessage("TestMail.subject", english);

			var email = new TestEmail(subject, TestKeys.TEST_MAIL_FTL, getParameters(), english);

			return sendMail(email, getParameters().get("caseId"));
		}

		Try<Void> sendTestEmailDe() {

			var locale = new Locale("de", "DE", "test");
			var subject = messages.getMessage("TestMail.subject", locale);

			var email = new TestEmail(subject, TestKeys.TEST_MAIL_FTL, getParameters(), locale);

			return sendMail(email, getParameters().get("caseId"));
		}

		Try<Void> sendTestHtmlEmailDe() {

			var locale = new Locale("de", "DE", "test");
			var subject = messages.getMessage("TestMail.subject", locale);

			var email = new TestEmail(subject, TestKeys.TEST_HTML_MAIL_FTLH, getParameters(), locale);

			return sendMail(email, getParameters().get("caseId"));
		}

		private Map<String, String> getParameters() {
			var parameters = Map.of("recipientName", "Name", "text", "Der Text", "senderName", "Sender");
			return parameters;
		}
	}

	class TestEmail extends AbstractTemplatedEmail {

		TestEmail(String subject, EmailTemplates.Key template, Map<String, ? extends Object> placeholders, Locale locale) {

			super(new TestRecipient(), subject, template, placeholders, locale);
		}
	}

	enum TestKeys
			implements
			Key {

		TEST_MAIL_FTL, TEST_HTML_MAIL_FTLH;

		@Override
		public String getKey() {
			return name().toLowerCase(Locale.US).replace("_ftl", ".ftl").replace('_', '-');
		}
	}

	class TestRecipient implements Recipient {

		@Override
		public String getFullName() {
			return "Test Person";
		}

		@Override
		public EmailAddress getEmailAddress() {
			return EmailAddress.of("test@test.de");
		}
	}
}
