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
package iris.client_bff.cases;

import io.vavr.control.Try;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.core.EmailAddress;
import iris.client_bff.core.mail.EmailProvider;
import iris.client_bff.core.mail.EmailSender;
import iris.client_bff.core.mail.EmailSender.AbstractTemplatedEmail.ConfiguredRecipient;
import iris.client_bff.core.mail.EmailTemplates;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * Helper to send out emails during index case processing
 * 
 * @author Jens Kutzsche
 */
@Component
@Slf4j
public class CaseEmailProvider extends EmailProvider {

	@Value("${iris.client.basePath}")
	private String basePath;

	@Value("${spring.mail.properties.recipient.case.data-recieved.name}")
	private String dataRecievedRecipientName;
	@Value("${spring.mail.properties.recipient.case.data-recieved.email}")
	private String dataRecievedRecipientEmail;

	public CaseEmailProvider(EmailSender emailSender, MessageSourceAccessor messages) {
		super(emailSender, messages);
	}

	public Try<Void> sendDataRecievedEmail(IndexCaseDetailsDTO caseData) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("caseId", caseData.getName());
		parameters.put("externalId", caseData.getExternalCaseId());
		parameters.put("startTime", caseData.getStart());
		parameters.put("endTime", caseData.getEnd());
		parameters.put("caseUrl", basePath + "/cases/details/" + caseData.getCaseId());

		var subject = messages.getMessage("CaseDataRecievedEmail.subject");

		CaseEmail email;

		if (dataRecievedRecipientName != null && dataRecievedRecipientEmail != null) {
			EmailAddress emailAddress = EmailAddress.of(dataRecievedRecipientEmail);
			ConfiguredRecipient recipient = new ConfiguredRecipient(dataRecievedRecipientName, emailAddress);

			email = new CaseEmail(recipient, subject, EmailTemplates.Keys.CASE_DATA_RECIEVED_MAIL_FTLH, parameters);
		} else {
			email = new CaseEmail(null, subject, EmailTemplates.Keys.CASE_DATA_RECIEVED_MAIL_FTLH, parameters);
		}

		return sendMail(email);
	}
}
