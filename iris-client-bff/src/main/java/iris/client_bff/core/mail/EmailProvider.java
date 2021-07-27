/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package iris.client_bff.core.mail;

import io.vavr.control.Try;
import iris.client_bff.core.EmailAddress;
import iris.client_bff.core.mail.EmailSender.AbstractTemplatedEmail.ConfiguredRecipient;
import iris.client_bff.core.mail.EmailSender.TemplatedEmail;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * @author Jens Kutzsche
 */
@Slf4j
@RequiredArgsConstructor
public class EmailProvider {

	@Value("${spring.mail.properties.limit.resending.attempts:5}")
	private Integer limitResendingAttempts = 5;

	@Value("${spring.mail.properties.limit.resending.delay:30000}")
	private Integer sleepBetweenAttempts = 3000;

	private final @NonNull EmailSender emailSender;
	final @NonNull protected MessageSourceAccessor messages;

	protected Try<Void> sendMail(TemplatedEmail email, ConfiguredRecipient recipient, String id) {

		Object[] logArgs = null;
		if (recipient != null) {
			logArgs = new Object[] {
				email.getTemplate(),
				recipient.getFullName(),
				recipient.getEmailAddress(),
				id };
		} else {
			ConfiguredRecipient standardRecipientForLogEntries =
				new ConfiguredRecipient("fix-recipient", EmailAddress.of("fix-recipient@iris-connect.de"));
			logArgs = new Object[] {
				email.getTemplate(),
				standardRecipientForLogEntries.getFullName(),
				standardRecipientForLogEntries.getEmailAddress(),
				id };
		}

		return sendTillSuccessOrLimitReached(email, logArgs, 0);
	}

	private Try<Void> sendTillSuccessOrLimitReached(TemplatedEmail email, Object[] logArgs, int attempts) {
		return emailSender.sendMail(email)
			.onSuccess(__ -> log.info("Mail of the template {} sent to {} ({}) for Event-Id/Case-ID {}", logArgs))
			.onFailure(e -> {
				if (attempts < limitResendingAttempts) {
					int count = attempts + 1;
					log.info(
						"The attempt number " + count
							+ " to send a mail of the template {} to {} ({}) for Event-Id/Case-ID {} failed. Retry will follow.",
						logArgs);
					try {
						Thread.sleep(30000);
						sendTillSuccessOrLimitReached(email, logArgs, count);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						sendTillSuccessOrLimitReached(email, logArgs, count);
					}
				} else {
					log.warn("Can't send a mail of the template {} to {} ({}) for Event-Id/Case-ID {}", logArgs);
					log.warn("Exception", e);
				}
			});
	}

}
