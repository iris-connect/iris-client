package iris.client_bff.core.mail;

import io.vavr.control.Try;
import iris.client_bff.core.mail.EmailTemplates.EmailType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
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
@ConditionalOnProperty(name = "iris.client.mailing.active", havingValue = "")
public class EmailSenderReal implements ApplicationListener<ApplicationReadyEvent>, EmailSender {

	private final @NonNull JavaMailSenderImpl internalSender;
	private final @NonNull EmailTemplates templates;
	private final @NonNull MailProperties mailProperties;

	private boolean initialized = false;

	/**
	 * Verifies the connection to the actual email server.
	 *
	 * @return an {@link Object} to represent the server information, will never be {@literal null}.
	 */
	@Override
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
	@Override
	public Try<Void> sendMail(TemplatedEmail email) {

		Assert.notNull(email, "Email must not be null!");

		return !initialized ? Try.success(null) : Try.run(() -> {

			if (email.toMailType(templates) == EmailType.HTML) {
				internalSender.send(email.toHmtlMail(templates, mailProperties, internalSender));
			} else {
				internalSender.send(email.toSimpleMail(templates, mailProperties));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		Assert.hasText(mailProperties.getProperties().get(FIX_SENDER_PROPERTY_KEY),
				MISSING_FIXED_SENDER);

		log.info("Email sender initialized and ready to send out emails.");

		this.initialized = true;
	}
}
