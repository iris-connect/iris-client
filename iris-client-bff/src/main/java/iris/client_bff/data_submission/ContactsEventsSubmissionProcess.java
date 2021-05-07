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
package iris.client_bff.data_submission;

import iris.client_bff.data_submission.dto.ContactPersonList;
import iris.client_bff.data_submission.dto.ContactsAndEvents;
import iris.client_bff.data_submission.dto.EventList;

import iris.client_bff.data_request.events.EventDataRequest;
import java.security.KeyStore;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
public class ContactsEventsSubmissionProcess extends DataSubmissionProcess<ContactsAndEvents> {

	public ContactsEventsSubmissionProcess(DataSubmissionDto submissionDto, EventDataRequest request, KeyStore keyStore,
			ObjectMapper mapper) {

		super(submissionDto, ContactsAndEvents.class, request, keyStore, mapper);
	}

	@Override
	public void process(ContactsAndEvents dto) {

		handleContacts(dto.getContacts());
		handleEvents(dto.getEvents());
	}

	/**
	 * @param contacts
	 */
	private void handleContacts(ContactPersonList contacts) {

	}

	/**
	 * @param events
	 */
	private void handleEvents(EventList events) {
		// TODO Auto-generated method stub

	}
}
