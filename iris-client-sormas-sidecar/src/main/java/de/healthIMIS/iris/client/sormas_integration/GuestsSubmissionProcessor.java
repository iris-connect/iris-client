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
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_submission.model.Guest;
import de.healthIMIS.iris.client.data_submission.model.GuestList;
import de.healthIMIS.iris.client.sormas_integration.DataSubmissionJob.DataSubmissionDto;
import de.healthIMIS.sormas.client.api.EventParticipantControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.EventParticipantDto;
import de.healthIMIS.sormas.client.model.EventReferenceDto;
import de.healthIMIS.sormas.client.model.PersonDto;
import de.healthIMIS.sormas.client.model.TaskContext;
import de.healthIMIS.sormas.client.model.TaskPriority;
import de.healthIMIS.sormas.client.model.TaskType;
import de.healthIMIS.sormas.client.model.UserReferenceDto;

/**
 * @author Jens Kutzsche
 */
public class GuestsSubmissionProcessor extends DataSubmissionProcessor {

	private EventParticipantControllerApi sormasParticipantApi;
	private PersonControllerApi sormasPersonApi;

	public GuestsSubmissionProcessor(
		DataSubmissionDto submissionDto,
		DataRequest request,
		KeyStore keyStore,
		ObjectMapper mapper,
		TaskControllerApi taskApi,
		EventParticipantControllerApi sormasParticipantApi,
		PersonControllerApi sormasPersonApi) {

		super(submissionDto, request, keyStore, mapper, taskApi);

		this.sormasParticipantApi = sormasParticipantApi;
		this.sormasPersonApi = sormasPersonApi;
	}

	@Override
	public void process() {

		GuestList guestList;
		try {

			var content = decryptContent(submissionDto.getEncryptedData(), submissionDto.getKeyReferenz(), submissionDto.getSecret());
			guestList = mapper.readValue(content, GuestList.class);

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

		var guests = guestList.getGuests();
		for (var guest : guests) {

			var person = createPersonDto(guest);
			var participant = createParticipantDto(person);

			System.out.println(sormasPersonApi.postPersons(List.of(person)));
			System.out.println(sormasParticipantApi.postEventParticipants(List.of(participant)));

			createTask(request, request.getRefId());
		}
	}

	private PersonDto createPersonDto(Guest guest) {
		var person = new PersonDto();
		person.setUuid(SormasRefId.random().toString());
//			person.setSex(Sex.UNKNOWN);
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

	private void createTask(DataRequest request, SormasRefId eventId) {

		var newTask = createTask(request);

		newTask.setCreatorComment("Teilnehmer zu Ereignis hinzugefügt, bitte weiter bearbeiten");
		newTask.setTaskType(TaskType.EVENT_CONTINUE_INVESTIGATION);

		var eventRef = new EventReferenceDto();
		eventRef.setUuid(eventId.toString());
		newTask.setEvent(eventRef);
		newTask.setTaskContext(TaskContext.EVENT);
		newTask.setPriority(TaskPriority.NORMAL);

		System.out.println(taskApi.postTasks(List.of(newTask)));
	}
}
