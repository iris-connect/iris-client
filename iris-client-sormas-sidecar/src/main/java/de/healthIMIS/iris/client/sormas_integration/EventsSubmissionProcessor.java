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

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_submission.model.Event;
import de.healthIMIS.iris.client.data_submission.model.EventList;
import de.healthIMIS.iris.client.sormas_integration.DataSubmissionJob.DataSubmissionDto;
import de.healthIMIS.sormas.client.api.EventControllerApi;
import de.healthIMIS.sormas.client.api.EventParticipantControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.CaseReferenceDto;
import de.healthIMIS.sormas.client.model.Disease;
import de.healthIMIS.sormas.client.model.EventDto;
import de.healthIMIS.sormas.client.model.EventParticipantDto;
import de.healthIMIS.sormas.client.model.EventReferenceDto;
import de.healthIMIS.sormas.client.model.EventStatus;
import de.healthIMIS.sormas.client.model.LocationDto;
import de.healthIMIS.sormas.client.model.TaskContext;
import de.healthIMIS.sormas.client.model.TaskPriority;
import de.healthIMIS.sormas.client.model.TaskType;
import de.healthIMIS.sormas.client.model.UserReferenceDto;

/**
 * @author Jens Kutzsche
 */
public class EventsSubmissionProcessor extends DataSubmissionProcessor {

	private EventControllerApi sormasEventApi;
	private EventParticipantControllerApi sormasParticipantApi;
	private PersonControllerApi sormasPersonApi;

	public EventsSubmissionProcessor(
		DataSubmissionDto submissionDto,
		DataRequest request,
		KeyStore keyStore,
		ObjectMapper mapper,
		TaskControllerApi taskApi,
		EventControllerApi sormasEventApi,
		EventParticipantControllerApi sormasParticipantApi,
		PersonControllerApi sormasPersonApi) {

		super(submissionDto, request, keyStore, mapper, taskApi);

		this.sormasEventApi = sormasEventApi;
		this.sormasParticipantApi = sormasParticipantApi;
		this.sormasPersonApi = sormasPersonApi;
	}

	@Override
	public void process() {

		EventList eventList;
		try {

			var content = decryptContent(submissionDto.getEncryptedData(), submissionDto.getKeyReferenz(), submissionDto.getSecret());
			eventList = mapper.readValue(content, EventList.class);

		} catch (JsonProcessingException
			| InvalidKeyException
			| UnrecoverableKeyException
			| NoSuchAlgorithmException
			| NoSuchPaddingException
			| InvalidKeySpecException
			| IllegalBlockSizeException
			| BadPaddingException
			| KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		for (var event : eventList.getEvents()) {

			var userRef = new UserReferenceDto();
			userRef.setUuid(request.getSormasUserId());

			var eventDto = createEventDto(event, userRef);
			var participant = createParticipantDto(eventDto, userRef);

			System.out.println(sormasEventApi.postEvents(List.of(eventDto)));
			System.out.println(sormasParticipantApi.postEventParticipants(List.of(participant)));

			createTask(request, eventDto);
		}
	}

	private EventDto createEventDto(Event event, UserReferenceDto userRef) {
		var eventDto = new EventDto();
		eventDto.setUuid(SormasRefId.random().toString());
		eventDto.setEventTitle(event.getName());
		eventDto.setEventDesc(event.getAdditionalInformation());
		eventDto.setDisease(Disease.CORONAVIRUS);
		eventDto.setEventStatus(EventStatus.EVENT);
		eventDto.setReportDateTime(Instant.now());

		var location = new LocationDto();
		location.setUuid(SormasRefId.random().toString());
		eventDto.setEventLocation(location);

		eventDto.setReportingUser(userRef);
		return eventDto;
	}

	private EventParticipantDto createParticipantDto(EventDto eventDto, UserReferenceDto userRef) {
		var participant = new EventParticipantDto();
		participant.setUuid(SormasRefId.random().toString());
		participant.setReportingUser(userRef);

		var eventRef = new EventReferenceDto();
		eventRef.setUuid(eventDto.getUuid());
		participant.setEvent(eventRef);

		var caseRef = new CaseReferenceDto();
		caseRef.setUuid(request.getRefId().toString());
		participant.setResultingCase(caseRef);

		var persons = sormasPersonApi.getByUuids18(List.of(request.getPersonId().toString()));

		if (persons.size() > 0) {
			participant.setPerson(persons.get(0));
		}
		return participant;
	}

	private void createTask(DataRequest request, EventDto event) {

		var newTask = createTask(request);

		newTask.setCreatorComment("Ereignis neu angelegt, bitte weiter bearbeiten");
		newTask.setTaskType(TaskType.EVENT_INVESTIGATION);

		var eventRef = new EventReferenceDto();
		eventRef.setUuid(event.getUuid());
		newTask.setEvent(eventRef);
		newTask.setTaskContext(TaskContext.EVENT);
		newTask.setPriority(TaskPriority.NORMAL);

		System.out.println(taskApi.postTasks(List.of(newTask)));
	}
}
