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
package de.healthIMIS.iris.client.sormas_integration;

import de.healthIMIS.iris.api.sidecarclient.model.ContactsAndEvents;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_submission.DataSubmissionDto;
import de.healthIMIS.sormas.client.api.ContactControllerApi;
import de.healthIMIS.sormas.client.api.EventControllerApi;
import de.healthIMIS.sormas.client.api.EventParticipantControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;

import java.security.KeyStore;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
// @Service
// @ConditionalOnBean(TaskControllerApi.class)
public class ContactsEventsSubmissionSormasProcessor extends DataSubmissionSormasProcessor<ContactsAndEvents> {

	private ContactsSubmissionProcessor contactsProcessor;
	private EventsSubmissionProcessor eventsProcessor;

	public ContactsEventsSubmissionSormasProcessor(DataSubmissionDto submissionDto, DataRequest request,
			KeyStore keyStore,
			ObjectMapper mapper, TaskControllerApi sormasTaskApi, PersonControllerApi sormasPersonApi,
			ContactControllerApi sormasContactApi, EventControllerApi sormasEventApi,
			EventParticipantControllerApi sormasParticipantApi) {

		super(submissionDto, ContactsAndEvents.class, request, keyStore, mapper, sormasTaskApi);

		contactsProcessor = new ContactsSubmissionProcessor(request, sormasTaskApi, sormasPersonApi, sormasContactApi);
		eventsProcessor = new EventsSubmissionProcessor(request, sormasTaskApi, sormasEventApi, sormasParticipantApi,
				sormasPersonApi);
	}

	@Override
	public void process(ContactsAndEvents dto) {

		contactsProcessor.process(dto.getContacts());
		eventsProcessor.process(dto.getEvents());
	}
}
