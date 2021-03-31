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
package de.healthIMIS.iris.client.data_submission;

import de.healthIMIS.iris.api.sidecarclient.model.ContactPersonList;
import de.healthIMIS.iris.api.sidecarclient.model.ContactsAndEvents;
import de.healthIMIS.iris.api.sidecarclient.model.EventList;
import de.healthIMIS.iris.client.data_request.DataRequest;

import java.security.KeyStore;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
public class ContactsEventsSubmissionProcessor extends DataSubmissionProcessor<ContactsAndEvents> {

	public ContactsEventsSubmissionProcessor(DataSubmissionDto submissionDto, DataRequest request, KeyStore keyStore,
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
