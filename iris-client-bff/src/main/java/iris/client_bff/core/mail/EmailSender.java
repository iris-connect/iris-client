package iris.client_bff.core.mail;

import io.vavr.control.Try;
import iris.client_bff.core.mail.EmailTemplates.Key;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Generic email sender for IRIS that uses the templates of {@link EmailTemplates} as body. The data for a email are
 * given with an instance of <code>EmailData</code>.
 * <p>
 * The EmailSender uses <strong>lastName = <code>getToLastName()</code> und host = {host from configuration}</strong> as
 * default placeholders with the template.
 * </p>
 *
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSender implements ApplicationListener<ApplicationReadyEvent> {

	private final @NonNull JavaMailSenderImpl internalSender;
	private final @NonNull EmailTemplates templates;
	private final @NonNull MailProperties mailProperties;

	private boolean initialized = false;

	/**
	 * Verifies the connection to the actual email server.
	 *
	 * @return an {@link Object} to represent the server information, will never be {@literal null}.
	 */
	public Try<Object> testConnection() {

		return Try.success(internalSender) //
				.filter(it -> it.getHost() != null, () -> new IllegalStateException("No email server host configured!"))
				.andThenTry(JavaMailSenderImpl::testConnection)
				.map(it -> new Object() {

					@Override
					@SuppressWarnings("null")
					public String toString() {
						return it.getHost() + ":" + it.getPort();
					}
				});
	}

	/**
	 * Sends the given {@link TemplatedEmail}.
	 *
	 * @param email must not be {@literal null}.
	 * @return
	 */
	public Try<Void> sendMail(TemplatedEmail email) {

		Assert.notNull(email, "Email must not be null!");

		return !initialized
				? Try.success(null)
				: Try.run(() -> internalSender.send(email.toMailMessage(templates, mailProperties)));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		log.info("Email sender initialized and ready to send out emails.");

		this.initialized = true;
	}

	/**
	 * An email to be composed from {@link EmailTemplates} and {@link CoreProperties}. Custom implementations usually
	 * extend {@link AbstractTemplatedEmail}.
	 *
	 * @author Jens Kutzsche
	 * @author Oliver Drotbohm
	 * @see AbstractTemplatedEmail
	 */
	public interface TemplatedEmail {

		Key getTemplate();

		/**
		 * Composes a {@link SimpleMailMessage} using the given {@link EmailTemplates} and {@link CoreProperties}.
		 *
		 * @param templates must not be {@literal null}.
		 * @param configuration must not be {@literal null}.
		 * @param mailProperties
		 * @param environment
		 * @return
		 */
		SimpleMailMessage toMailMessage(EmailTemplates templates, MailProperties mailProperties);

		String getBody(EmailTemplates templates);
	}

	/**
	 * Convenient base class to compose emails from a template defined by a {@link Key} and placeholders to expand the
	 * template with. Sender and Receiver are abstracted to be composed separately and handed into the instance.
	 *
	 * @author Jens Kutzsche
	 * @author Oliver Drotbohm
	 */
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static abstract class AbstractTemplatedEmail implements TemplatedEmail {

		/**
		 * At the moment we can only use a configured fix sender address, to avoid problems with permissions. This comes
		 * from spam protection. With other sender addresses we get the following error:
		 * <code>550 5.7.60 SMTP; Client does not
		 * have permissions to send as this sender</code>. To configure the fix sender address use
		 * <code>spring.mail.properties.fix-sender</code>.
		 */
		private static final String FIX_SENDER_PROPERTY_KEY = "fix-sender";
		private static final String FIX_SENDER_NAME_PROPERTY_KEY = "fix-sender-name";
		static final String FIX_RECIPIENT_PROPERTY_KEY = "fix-recipient";

		private final @Getter String from;
		private final @Getter String to;
		private final @Getter String subject;
		private final @Getter Key template;
		private final @Getter Map<String, ? extends Object> placeholders;
		private final Locale locale;

		@Override
		public SimpleMailMessage toMailMessage(EmailTemplates templates, @NonNull MailProperties mailProperties) {

			var message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(getBody(templates));
			message.setSentDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

			if (log.isDebugEnabled()) {
				log.debug("Mail message created: " + message);
			}

			return message;
		}

		@Override
		public String getBody(EmailTemplates templates) {

			return "";
		}
	}
}
