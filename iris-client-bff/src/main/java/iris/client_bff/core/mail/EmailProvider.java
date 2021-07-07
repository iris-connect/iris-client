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

	@Value("${spring.mail.properties.limit.resending.attempts}")
	private Integer limitResendingAttempts;

	private final @NonNull EmailSender emailSender;
	final @NonNull protected MessageSourceAccessor messages;

	protected Try<Void> sendMail(TemplatedEmail email) {

		var logArgs = new Object[] {
			email.getTemplate(),
			"" };

		return sendTillSuccessOrLimitReached(email, logArgs, 0);
	}

	private Try<Void> sendTillSuccessOrLimitReached(TemplatedEmail email, Object[] logArgs, int attempts) {
		return emailSender.sendMail(email).onSuccess(__ -> log.info("Mail {} sent to {{}; {}; Case-ID {}}", logArgs)).onFailure(e -> {
			if (limitResendingAttempts == null) {
				limitResendingAttempts = 5;
			}
			if (attempts < limitResendingAttempts) {
				int count = attempts + 1;
				log.info("Attempt " + count + " to send mail {} to {{}; {}; Case-ID {}} failed. Retry will follow.", logArgs);
				sendTillSuccessOrLimitReached(email, logArgs, count);
			} else {
				log.warn("Can't send mail {} to {{}; {}; Case-ID {}}", logArgs);
				log.warn("Exception", e);
			}
		});
	}

}
