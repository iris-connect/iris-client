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

import de.healthIMIS.iris.api.sidecarclient.model.ContactPerson;
import de.healthIMIS.iris.api.sidecarclient.model.ContactPersonList;
import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.sormas.client.api.ContactControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.*;

import java.time.Instant;
import java.util.List;

/**
 * @author Jens Kutzsche
 */
public class ContactsSubmissionProcessor extends DataSubmissionSubProcessor<ContactPersonList> {

	private PersonControllerApi sormasPersonApi;
	private ContactControllerApi sormasContactApi;

	public ContactsSubmissionProcessor(DataRequest request, TaskControllerApi taskApi,
			PersonControllerApi sormasPersonApi, ContactControllerApi sormasContactApi) {

		super(request, taskApi);

		this.sormasPersonApi = sormasPersonApi;
		this.sormasContactApi = sormasContactApi;
	}

	@Override
	public void process(ContactPersonList contactList) {

		for (var contactPerson : contactList.getContactPersons()) {

			var person = createPersonDto(contactPerson);
			var contact = createContactDto(person);

			System.out.println(sormasPersonApi.postPersons(List.of(person)));
			System.out.println(sormasContactApi.postContacts(List.of(contact)));

			createAndTransmitTask(it -> customizeTask(it, contact));
		}
	}

	private PersonDto createPersonDto(ContactPerson contactPerson) {
		var person = new PersonDto();
		person.setUuid(SormasRefId.random().toString());
		// person.setSex(Sex.UNKNOWN);
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

	private TaskDto customizeTask(TaskDto task, ContactDto contact) {

		task.setCreatorComment("Kontakt neu angelegt, bitte weiter bearbeiten");
		task.setTaskType(TaskType.CONTACT_FOLLOW_UP);

		var contRef = new ContactReferenceDto();
		contRef.setUuid(contact.getUuid());
		task.setContact(contRef);
		task.setTaskContext(TaskContext.CONTACT);
		task.setPriority(TaskPriority.NORMAL);

		return task;
	}
}
