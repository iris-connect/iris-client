package iris.client_bff.core.mail.support;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Arrays;

import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

/**
 * <p>
 * Starts a SMTPS-Server with a random port. Use the following properties to use this with Spring Mail:
 * <code>spring.mail.host=127.0.0.1 spring.mail.port={random port is set} spring.mail.properties.mail.smtp.socketFactory.class = iris.client_bff.core.mail.support.GreenMailSSLSocketFactory</code>
 * </p>
 * <p>
 * This starts also an IMAPS-Server with port 3993 or a random port if 3993 is occupied. You can use it with an e-mail
 * program to view the received e-mails: <code>host=127.0.0.1 port=3993</code> For each new e-mail address of the
 * received mails, a new account is created from which the mails can be retrieved. Username and password are the mail
 * address. You can also see this in the log outputs.
 * </p>
 * <p>
 * The received e-mails are logged with their basic data (from, to, date and subject).
 * </p>
 * 
 * @author Jens Kutzsche
 */
@Slf4j
@Service()
@Profile("dev_env")
class GreenMailEmailServer implements FactoryBean<GreenMail>, DisposableBean {

	private static final int DELETE_AFTER_SECONDS = 600;

	private GreenMail greenMail;

	GreenMailEmailServer(MailProperties properties) {
		log.debug("GreenMail: Mail server is started…");

		var smtp = ServerSetup.SMTPS.dynamicPort();
		var imap = ServerSetup.IMAPS.withPort(3993);

		try {
			// if default port is available
			SocketUtils.findAvailableTcpPort(imap.getPort(), imap.getPort());
		} catch (Exception e) {
			imap = imap.dynamicPort();
		}

		greenMail = new GreenMail(
				ServerSetup.verbose(
						new ServerSetup[] {
								smtp,
								imap }));
		greenMail.start();

		var smtpPort = greenMail.getSmtps().getPort();
		var imapPort = greenMail.getImaps().getPort();

		properties.setPort(smtpPort);

		log.info("GreenMail: Mail server successfully started on localhost: SMTPS = {}; IMAPS = {}", smtpPort, imapPort);
	}

	@Scheduled(fixedDelay = 15000)
	public void receiveAndLogMessages() {

		if (greenMail != null) {

			var deleteThreshold = Instant.now().minusSeconds(DELETE_AFTER_SECONDS);
			var messages = greenMail.getReceivedMessages();

			Arrays.stream(messages)
					.filter(it -> deleteOldMessages(it, deleteThreshold))
					.filter(GreenMailEmailServer::isMailNew)
					.peek(GreenMailEmailServer::markSeen)
					.forEach(GreenMailEmailServer::logMessage);
		}
	}

	@Override
	public GreenMail getObject() throws Exception {
		return greenMail;
	}

	@Override
	public Class<?> getObjectType() {
		return GreenMail.class;
	}

	@Override
	public void destroy() {

		if (greenMail != null) {

			log.debug("GreenMail: Mail server is stopped…");

			greenMail.stop();

			log.info("GreenMail: Mail server successfully stopped!");
		}
	}

	private static boolean deleteOldMessages(MimeMessage message, Instant deleteThreshold) {

		try {

			if (message.getReceivedDate().toInstant().isBefore(deleteThreshold)) {

				message.setFlag(Flag.DELETED, true);
				return false;
			}

		} catch (MessagingException e) {}

		return true;
	}

	private static boolean isMailNew(MimeMessage message) {

		try {
			return !message.isSet(Flag.SEEN);
		} catch (MessagingException e) {
			return false;
		}
	}

	private static void markSeen(MimeMessage message) {

		try {
			message.setFlag(Flag.SEEN, true);
		} catch (MessagingException e) {}
	}

	private static void logMessage(MimeMessage message) {

		try {

			var from = Arrays.toString(message.getFrom());
			var to = Arrays.toString(message.getAllRecipients());
			var date = message.getReceivedDate() == null ? ""
					: DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(message.getReceivedDate());
			var subject = String.valueOf(message.getSubject());

			log.debug("Mail received {from {}; to {}; at {}; subject {}}", from, to, date, subject);

		} catch (MessagingException e) {
			log.debug("Mail received but an exception occurred during reception.");
		}
	}
}
