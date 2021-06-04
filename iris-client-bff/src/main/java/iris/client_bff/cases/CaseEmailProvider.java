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
import iris.client_bff.core.mail.EmailProvider;
import iris.client_bff.core.mail.EmailSender;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

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

	public CaseEmailProvider(EmailSender emailSender, MessageSourceAccessor messages) {
		super(emailSender, messages);
	}

	public Try<Void> sendDataRecievedEmail(IndexCaseDetailsDTO caseData) {
		var parameters = Collections.<String, Object> emptyMap();
		parameters.put("caseId", caseData.getName());
		parameters.put("externalId", caseData.getExternalCaseId());
		parameters.put("startTime", caseData.getStart());
		parameters.put("endTime", caseData.getEnd());
		var subject = messages.getMessage("CaseDataRecievedEmail.subject");

		var email = new CaseEmail(subject, null, parameters);

		return sendMail(email);
	}
}
