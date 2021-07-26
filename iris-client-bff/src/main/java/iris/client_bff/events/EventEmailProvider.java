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
package iris.client_bff.events;

import io.vavr.control.Try;
import iris.client_bff.core.EmailAddress;
import iris.client_bff.core.mail.EmailProvider;
import iris.client_bff.core.mail.EmailSender;
import iris.client_bff.core.mail.EmailSender.AbstractTemplatedEmail.ConfiguredRecipient;
import iris.client_bff.core.mail.EmailTemplates;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 * Helper to send out emails during event processing
 * 
 * @author Jens Kutzsche
 */
@Component
@Slf4j
public class EventEmailProvider extends EmailProvider {

	@Value("${iris.client.basePath}")
	private String basePath;

	@Value("${spring.mail.properties.recipient.event.data-received.name}")
	private String dataReceivedRecipientName;
	@Value("${spring.mail.properties.recipient.event.data-received.email}")
	private String dataReceivedRecipientEmail;

	public EventEmailProvider(EmailSender emailSender, MessageSourceAccessor messages) {
		super(emailSender, messages);
	}

	@Async
	public Future<Try<Void>> sendDataReceivedEmailAsynchronously(EventDataRequest eventData) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventData.getName());
		parameters.put("externalId", eventData.getRefId());
		parameters.put("startTime", eventData.getRequestStart());
		parameters.put("endTime", eventData.getRequestEnd());
		parameters.put("eventUrl", basePath + "/events/details/" + eventData.getId());

		var subject = messages.getMessage("EventDataReceivedEmail.subject");

		EventEmail email;

		if (dataReceivedRecipientName != null && dataReceivedRecipientEmail != null) {
			EmailAddress emailAddress = EmailAddress.of(dataReceivedRecipientEmail);
			ConfiguredRecipient recipient = new ConfiguredRecipient(dataReceivedRecipientName, emailAddress);

			email = new EventEmail(recipient, subject, EmailTemplates.Keys.EVENT_DATA_RECEIVED_MAIL_FTLH, parameters);
			return new AsyncResult<Try<Void>>(sendMail(email, recipient, parameters.get("caseId").toString()));
		} else {
			email = new EventEmail(null, subject, EmailTemplates.Keys.EVENT_DATA_RECEIVED_MAIL_FTLH, parameters);
			return new AsyncResult<Try<Void>>(sendMail(email, null, parameters.get("eventId").toString()));
		}
	}
}
