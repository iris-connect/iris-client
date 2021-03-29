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

import de.healthIMIS.iris.api.sidecarclient.model.Guest;
import de.healthIMIS.iris.api.sidecarclient.model.GuestList;
import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.sormas_integration.DataSubmissionJob.DataSubmissionDto;
import de.healthIMIS.sormas.client.api.EventParticipantControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.EventParticipantDto;
import de.healthIMIS.sormas.client.model.EventReferenceDto;
import de.healthIMIS.sormas.client.model.PersonDto;
import de.healthIMIS.sormas.client.model.TaskContext;
import de.healthIMIS.sormas.client.model.TaskDto;
import de.healthIMIS.sormas.client.model.TaskPriority;
import de.healthIMIS.sormas.client.model.TaskType;
import de.healthIMIS.sormas.client.model.UserReferenceDto;

import java.security.KeyStore;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
public class GuestsSubmissionProcessor extends DataSubmissionProcessor<GuestList> {

	private EventParticipantControllerApi sormasParticipantApi;
	private PersonControllerApi sormasPersonApi;

	public GuestsSubmissionProcessor(DataSubmissionDto submissionDto, DataRequest request, KeyStore keyStore,
			ObjectMapper mapper, TaskControllerApi taskApi, EventParticipantControllerApi sormasParticipantApi,
			PersonControllerApi sormasPersonApi) {

		super(submissionDto, GuestList.class, request, keyStore, mapper, taskApi);

		this.sormasParticipantApi = sormasParticipantApi;
		this.sormasPersonApi = sormasPersonApi;
	}

	@Override
	public void process(GuestList guestList) {

		for (var guest : guestList.getGuests()) {

			var person = createPersonDto(guest);
			var participant = createParticipantDto(person);

			System.out.println(sormasPersonApi.postPersons(List.of(person)));
			System.out.println(sormasParticipantApi.postEventParticipants(List.of(participant)));
		}

		createAndTransmitTask(it -> customizeTask(it, request.getRefId()));
	}

	private PersonDto createPersonDto(Guest guest) {
		var person = new PersonDto();
		person.setUuid(SormasRefId.random().toString());
		// person.setSex(Sex.UNKNOWN);
		person.setAddresses(List.of());
		person.setFirstName(guest.getFirstName());
		person.setLastName(guest.getLastName());
		return person;
	}

	private EventParticipantDto createParticipantDto(PersonDto person) {
		var participant = new EventParticipantDto();
		participant.setUuid(SormasRefId.random().toString());
		participant.setPerson(person);

		var userRef = new UserReferenceDto();
		userRef.setUuid(request.getSormasUserId());
		participant.setReportingUser(userRef);

		var eventRef = new EventReferenceDto();
		eventRef.setUuid(request.getRefId().toString());
		participant.setEvent(eventRef);
		return participant;
	}

	private TaskDto customizeTask(TaskDto task, SormasRefId eventId) {

		task.setCreatorComment("Teilnehmer zu Ereignis hinzugefügt, bitte weiter bearbeiten");
		task.setTaskType(TaskType.EVENT_CONTINUE_INVESTIGATION);

		var eventRef = new EventReferenceDto();
		eventRef.setUuid(eventId.toString());
		task.setEvent(eventRef);
		task.setTaskContext(TaskContext.EVENT);
		task.setPriority(TaskPriority.NORMAL);

		return task;
	}
}
