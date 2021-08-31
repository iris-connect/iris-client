package iris.client_bff.events;

import iris.client_bff.core.mail.EmailSender.AbstractTemplatedEmail;
import iris.client_bff.core.mail.EmailTemplates;

import java.util.Map;

class EventEmail extends AbstractTemplatedEmail {

	EventEmail(Recipient recipient, String subject, EmailTemplates.Key template,
			Map<String, ? extends Object> placeholders) {

		super(recipient, subject, template, placeholders, null);
	}
}
