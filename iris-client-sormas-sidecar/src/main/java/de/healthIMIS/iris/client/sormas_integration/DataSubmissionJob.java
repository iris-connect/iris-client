/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.healthIMIS.iris.client.core.IrisClientProperties;
import de.healthIMIS.iris.client.core.IrisProperties;
import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.data_submission.model.ContactPerson;
import de.healthIMIS.iris.client.data_submission.model.ContactPersonList;
import de.healthIMIS.sormas.client.api.CaseControllerApi;
import de.healthIMIS.sormas.client.api.ContactControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.SampleControllerApi;
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
import de.healthIMIS.sormas.client.model.TaskDto;
import de.healthIMIS.sormas.client.model.TaskPriority;
import de.healthIMIS.sormas.client.model.TaskStatus;
import de.healthIMIS.sormas.client.model.TaskType;
import de.healthIMIS.sormas.client.model.UserReferenceDto;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@Profile("!inttest")
class DataSubmissionJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull CaseControllerApi sormasCaseApi;
	private final @NonNull PersonControllerApi sormasPersonApi;
	private final @NonNull TaskControllerApi sormasTaskApi;
	private final @NonNull SampleControllerApi sormasSampleApi;
	private final @NonNull ContactControllerApi sormasContactApi;
	private final @NonNull DataRequestManagement dataRequests;
	private final @NonNull RestTemplate rest;
	private final @NonNull IrisClientProperties clientProperties;
	private final @NonNull IrisProperties properties;
	private final @NonNull ObjectMapper mapper;
	private final @NonNull KeyStore keyStore;

	private long errorCounter = 0;

	public DataSubmissionJob(
		@NonNull SyncTimesRepository syncTimes,
		@NonNull CaseControllerApi sormasCaseApi,
		@NonNull PersonControllerApi sormasPersonApi,
		@NonNull TaskControllerApi sormasTaskApi,
		@NonNull SampleControllerApi sormasSampleApi,
		@NonNull ContactControllerApi sormasContactApi,
		@NonNull DataRequestManagement dataRequests,
		@NonNull @Qualifier("iris-rest") RestTemplate rest,
		@NonNull IrisClientProperties clientProperties,
		@NonNull IrisProperties properties,
		@NonNull ObjectMapper mapper,
		@NonNull KeyStore keyStore) {

		this.syncTimes = syncTimes;
		this.sormasCaseApi = sormasCaseApi;
		this.sormasPersonApi = sormasPersonApi;
		this.sormasTaskApi = sormasTaskApi;
		this.sormasSampleApi = sormasSampleApi;
		this.sormasContactApi = sormasContactApi;
		this.dataRequests = dataRequests;
		this.rest = rest;
		this.clientProperties = clientProperties;
		this.properties = properties;
		this.mapper = mapper;
		this.keyStore = keyStore;
	}

	@Scheduled(fixedDelay = 15000)
	void run() {

		log.trace("Submission job - start");

		var lastSync = syncTimes.findById(SyncTimes.DataTypes.Submissions)
			.map(SyncTimes::getLastSync)
			.map(it -> it.atZone(ZoneId.systemDefault()).toLocalDateTime())
			.orElse(LocalDateTime.of(2000, 01, 01, 00, 00));

		try {

			log.trace("Submission job - GET to server is sent");

			var response = rest.getForEntity(
				"https://{address}:{port}/hd/data-submissions?departmentId={depId}&from={from}",
				DataSubmissionDto[].class,
				properties.getServerAddress().getHostName(),
				properties.getServerPort(),
				clientProperties.getClientId(),
				lastSync);

			handleDtos(response.getBody());

			saveLastSync(response);

			log.debug(
				"Submission job - GET to public server sent: {}",
				Arrays.stream(response.getBody()).map(it -> it.getRequestId().toString()).collect(Collectors.joining(", ")));

			callDeleteEndpoint(lastSync);

			errorCounter = 0;

		} catch (RestClientException e) {

			errorCounter++;

			e.printStackTrace();

			if (errorCounter == 1 || errorCounter % 20 == 0) {
				if (log.isTraceEnabled()) {
					log.warn(String.format("Submission job - can't submit data submissions from server for %s tries!", errorCounter), e);
				} else {
					log.warn("Submission job - can't submit data submissions from server for {} tries!", errorCounter);
				}
			}
		}
	}

	private void handleDtos(DataSubmissionDto[] dtos) {

		Arrays.stream(dtos).forEach(it -> {

			ContactPersonList contactList;
			try {
				contactList = mapper.readValue(decryptContent(it.getEncryptedData(), it.getKeyReferenz()), ContactPersonList.class);
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

			var contacts = contactList.getContactPersons();
			for (ContactPerson contactPerson : contacts) {

				var request = dataRequests.findById(it.getRequestId()).get();

				var person = new PersonDto();
				person.setUuid(SormasRefId.of().toString());
//				person.setSex(Sex.UNKNOWN);
				person.setAddresses(List.of());
				person.setFirstName(contactPerson.getFirstName());
				person.setLastName(contactPerson.getLastName());

				var contact = new ContactDto();
				contact.setUuid(SormasRefId.of().toString());
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
				healtConditions.setUuid(SormasRefId.of().toString());
				contact.setHealthConditions(healtConditions);

				System.out.println(sormasPersonApi.postPersons(List.of(person)));
				System.out.println(sormasContactApi.postContacts(List.of(contact)));

				createTask(request, contact);

			}
		});
	}

	private String decryptContent(String content, String keyReferenz)
		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException, UnrecoverableKeyException, KeyStoreException {

		var encryptedArray = Base64.getDecoder().decode(content);

		var privateKey = keyStore.getKey(keyReferenz, null);

		var cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] decryptedArray = cipher.doFinal(encryptedArray);
		String decryptedString = new String(decryptedArray);
		System.out.println("Info Decrypt");
		System.out.println("Decrypted Array length: " + decryptedArray.length);
		System.out.println("Decrypted String:       " + decryptedString);

		return decryptedString;
	}

	private void createTask(DataRequest request, ContactDto contact) {

		var newTask = new TaskDto();

		newTask.setUuid(SormasRefId.of().toString());

		var nowDate = ZonedDateTime.now().withSecond(0);
		newTask.setDueDate(nowDate.toInstant());
		newTask.setPerceivedStart(nowDate.plusDays(1).toInstant());

		newTask.setTaskStatus(TaskStatus.PENDING);

		var userRef = new UserReferenceDto();
		userRef.setUuid(request.getSormasUserId());
		newTask.setAssigneeUser(userRef);

		userRef = new UserReferenceDto();
		userRef.setUuid(request.getIrisUserId());
		newTask.setCreatorUser(userRef);

		newTask.setCreatorComment("Kontakt neu angelegt, bitte weiter bearbeiten");
		newTask.setTaskType(TaskType.CONTACT_FOLLOW_UP);

		var contRef = new ContactReferenceDto();
		contRef.setUuid(contact.getUuid());
		newTask.setContact(contRef);
		newTask.setTaskContext(TaskContext.CONTACT);
		newTask.setPriority(TaskPriority.NORMAL);

		System.out.println(sormasTaskApi.postTasks(List.of(newTask)));
	}

	private void saveLastSync(ResponseEntity<DataSubmissionDto[]> response) {

		var lastModified = response.getHeaders().getLastModified();

		var lastSync = Instant.ofEpochMilli(lastModified);

		syncTimes.save(SyncTimes.of(SyncTimes.DataTypes.Submissions, lastSync));
	}

	private void callDeleteEndpoint(LocalDateTime lastSync) {

		log.trace("Submission job - DELETE to public server is sent");

		rest.delete(
			"https://{address}:{port}/hd/data-submissions?departmentId={depId}&from={from}",
			properties.getServerAddress().getHostName(),
			properties.getServerPort(),
			clientProperties.getClientId(),
			lastSync);

		log.trace("Submission job - DELETE to public server sent");
	}

	@Data
	static class DataSubmissionDto {

		private final UUID id;

		private final UUID requestId;

		private final UUID departmentId;

		private final String salt;

		private final String keyReferenz;

		private final String encryptedData;

		private final Feature feature;
	}

	public enum Feature {
		Contact,
		Events,
		Guests
	}
}
