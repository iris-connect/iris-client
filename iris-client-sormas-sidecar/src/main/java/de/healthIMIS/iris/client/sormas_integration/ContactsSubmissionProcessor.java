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
import de.healthIMIS.iris.client.data_submission.model.ContactPerson;
import de.healthIMIS.iris.client.data_submission.model.ContactPersonList;
import de.healthIMIS.iris.client.sormas_integration.DataSubmissionJob.DataSubmissionDto;
import de.healthIMIS.sormas.client.api.ContactControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.CaseReferenceDto;
import de.healthIMIS.sormas.client.model.ContactClassification;
import de.healthIMIS.sormas.client.model.ContactDto;
import de.healthIMIS.sormas.client.model.ContactReferenceDto;
import de.healthIMIS.sormas.client.model.Disease;
import de.healthIMIS.sormas.client.model.HealthConditionsDto;
import de.healthIMIS.sormas.client.model.PersonDto;
import de.healthIMIS.sormas.client.model.PersonReferenceDto;
import de.healthIMIS.sormas.client.model.TaskContext;
import de.healthIMIS.sormas.client.model.TaskPriority;
import de.healthIMIS.sormas.client.model.TaskType;
import de.healthIMIS.sormas.client.model.UserReferenceDto;

/**
 * @author Jens Kutzsche
 */
public class ContactsSubmissionProcessor extends DataSubmissionProcessor {

	private PersonControllerApi sormasPersonApi;
	private ContactControllerApi sormasContactApi;

	public ContactsSubmissionProcessor(
		DataSubmissionDto submissionDto,
		DataRequest request,
		KeyStore keyStore,
		ObjectMapper mapper,
		TaskControllerApi taskApi,
		PersonControllerApi sormasPersonApi,
		ContactControllerApi sormasContactApi) {

		super(submissionDto, request, keyStore, mapper, taskApi);

		this.sormasPersonApi = sormasPersonApi;
		this.sormasContactApi = sormasContactApi;
	}

	@Override
	public void process() {

		ContactPersonList contactList;
		try {

			var content = decryptContent(submissionDto.getEncryptedData(), submissionDto.getKeyReferenz(), submissionDto.getSecret());
			contactList = mapper.readValue(content, ContactPersonList.class);

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

		for (var contactPerson : contactList.getContactPersons()) {

			var person = createPersonDto(contactPerson);
			var contact = createContactDto(person);

			System.out.println(sormasPersonApi.postPersons(List.of(person)));
			System.out.println(sormasContactApi.postContacts(List.of(contact)));

			createTask(request, contact);
		}
	}

	private PersonDto createPersonDto(ContactPerson contactPerson) {
		var person = new PersonDto();
		person.setUuid(SormasRefId.random().toString());
//			person.setSex(Sex.UNKNOWN);
		person.setAddresses(List.of());
		person.setFirstName(contactPerson.getFirstName());
		person.setLastName(contactPerson.getLastName());
		return person;
	}

	private ContactDto createContactDto(PersonDto person) {
		var contact = new ContactDto();
		contact.setUuid(SormasRefId.random().toString());
		contact.setDisease(Disease.CORONAVIRUS);
		contact.setReportDateTime(Instant.now());
		contact.setContactClassification(ContactClassification.UNCONFIRMED);

		var persRef = new PersonReferenceDto();
		persRef.setUuid(person.getUuid());
		contact.setPerson(persRef);

		var caseRef = new CaseReferenceDto();
		caseRef.setUuid(request.getRefId().toString());
		contact.setCaze(caseRef);

		var userRef = new UserReferenceDto();
		userRef.setUuid(request.getIrisUserId());
		contact.setReportingUser(userRef);

		var healtConditions = new HealthConditionsDto();
		healtConditions.setCreationDate(Instant.now());
		healtConditions.setUuid(SormasRefId.random().toString());
		contact.setHealthConditions(healtConditions);
		return contact;
	}

	private void createTask(DataRequest request, ContactDto contact) {

		var newTask = createTask(request);

		newTask.setCreatorComment("Kontakt neu angelegt, bitte weiter bearbeiten");
		newTask.setTaskType(TaskType.CONTACT_FOLLOW_UP);

		var contRef = new ContactReferenceDto();
		contRef.setUuid(contact.getUuid());
		newTask.setContact(contRef);
		newTask.setTaskContext(TaskContext.CONTACT);
		newTask.setPriority(TaskPriority.NORMAL);

		System.out.println(taskApi.postTasks(List.of(newTask)));
	}
}
