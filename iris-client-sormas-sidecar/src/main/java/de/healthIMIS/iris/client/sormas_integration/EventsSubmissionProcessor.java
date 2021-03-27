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

import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_submission.model.Event;
import de.healthIMIS.iris.client.data_submission.model.EventList;
import de.healthIMIS.sormas.client.api.EventControllerApi;
import de.healthIMIS.sormas.client.api.EventParticipantControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.*;

import java.time.Instant;
import java.util.List;

/**
 * @author Jens Kutzsche
 */
public class EventsSubmissionProcessor extends DataSubmissionSubProcessor<EventList> {

	private EventControllerApi sormasEventApi;
	private EventParticipantControllerApi sormasParticipantApi;
	private PersonControllerApi sormasPersonApi;

	public EventsSubmissionProcessor(DataRequest request, TaskControllerApi taskApi, EventControllerApi sormasEventApi,
			EventParticipantControllerApi sormasParticipantApi, PersonControllerApi sormasPersonApi) {

		super(request, taskApi);

		this.sormasEventApi = sormasEventApi;
		this.sormasParticipantApi = sormasParticipantApi;
		this.sormasPersonApi = sormasPersonApi;
	}

	@Override
	public void process(EventList eventList) {

		for (var event : eventList.getEvents()) {

			var userRef = new UserReferenceDto();
			userRef.setUuid(request.getSormasUserId());

			var eventDto = createEventDto(event, userRef);
			var participant = createParticipantDto(eventDto, userRef);

			System.out.println(sormasEventApi.postEvents(List.of(eventDto)));
			System.out.println(sormasParticipantApi.postEventParticipants(List.of(participant)));

			createAndTransmitTask(it -> customizeTask(it, eventDto));
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

	private TaskDto customizeTask(TaskDto task, EventDto event) {

		task.setCreatorComment("Ereignis neu angelegt, bitte weiter bearbeiten");
		task.setTaskType(TaskType.EVENT_INVESTIGATION);

		var eventRef = new EventReferenceDto();
		eventRef.setUuid(event.getUuid());
		task.setEvent(eventRef);
		task.setTaskContext(TaskContext.EVENT);
		task.setPriority(TaskPriority.NORMAL);

		return task;
	}
}
