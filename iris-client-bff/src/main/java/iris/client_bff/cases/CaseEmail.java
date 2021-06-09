package iris.client_bff.cases;

import iris.client_bff.core.mail.EmailSender.AbstractTemplatedEmail;
import iris.client_bff.core.mail.EmailTemplates;

import java.util.Map;

class CaseEmail extends AbstractTemplatedEmail {

	CaseEmail(Recipient recipient, String subject, EmailTemplates.Key template, Map<String, ? extends Object> placeholders) {

		super(recipient, subject, template, placeholders, null);
	}
}
