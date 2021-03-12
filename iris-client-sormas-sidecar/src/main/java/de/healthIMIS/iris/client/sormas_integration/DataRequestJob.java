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

import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.MEDIUM;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.joinWith;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.sormas_integration.SyncTimes.DataTypes;
import de.healthIMIS.sormas.client.api.CaseControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.SampleControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.CaseDataDto;
import de.healthIMIS.sormas.client.model.PathogenTestResultType;
import de.healthIMIS.sormas.client.model.PersonDto;
import de.healthIMIS.sormas.client.model.SampleDto;
import de.healthIMIS.sormas.client.model.TaskDto;
import de.healthIMIS.sormas.client.model.TaskPriority;
import de.healthIMIS.sormas.client.model.TaskStatus;
import de.healthIMIS.sormas.client.model.TaskType;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!inttest")
class DataRequestJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull CaseControllerApi sormasCaseApi;
	private final @NonNull PersonControllerApi sormasPersonApi;
	private final @NonNull TaskControllerApi taskControllerApi;
	private final @NonNull SampleControllerApi sampleControllerApi;
	private final @NonNull DataRequestManagement dataRequests;

	private long errorCounter = 0;

	@Scheduled(fixedDelay = 15000)
	void run() {

		log.trace("Request job - start");

		var lastTime = syncTimes.findById(DataTypes.Tasks).map(SyncTimes::getLastSync).map(Instant::toEpochMilli).orElse(0l);

		var tasks = taskControllerApi.getAll7(lastTime);

		try {

			tasks.stream().sorted(Comparator.comparing(TaskDto::getChangeDate)).map(this::handleTask).forEach(it -> {

				if (it.getChangeDate() != null) {
					syncTimes.save(SyncTimes.of(DataTypes.Tasks, it.getChangeDate()));
				}
			});

		} catch (RestClientException e) {

			errorCounter++;

			e.printStackTrace();

			if (errorCounter == 1 || errorCounter % 20 == 0) {
				if (log.isTraceEnabled()) {
					log.warn(String.format("Request job - can't handle task and create data requests for %s tries!", errorCounter), e);
				} else {
					log.warn("Request job - can't handle task and create data requests for {} tries!", errorCounter);
				}
			}
		}
	}

	private TaskDto handleTask(TaskDto task) {

		if (isRelevant(task)) {

			switch (task.getTaskContext()) {
			case CASE: {
				handleCaseTask(task);
			}
			default:
			}
		}

		return task;
	}

	private void handleCaseTask(TaskDto task) {

		var cases = sormasCaseApi.getByUuids6(List.of(task.getCaze().getUuid()));

		if (!cases.isEmpty()) {

			var caseDto = cases.get(0);
			var persons = sormasPersonApi.getByUuids18(List.of(caseDto.getPerson().getUuid()));

			if (!persons.isEmpty()) {

				var person = persons.get(0);

				var caseId = SormasRefId.of(caseDto.getUuid());
				var sormasUserId = task.getCreatorUser().getUuid();
				var irisUserId = task.getAssigneeUser().getUuid();
				var checkCodeName = calculateNameCheckCodes(person);
				var checkCodeBirthday = calculateBirthdayCheckCodes(person);

				var startDate = firstRelevantSymptomDate(caseDto).orElse(positivSampleDate(caseDto)).get();

				var dataRequest = dataRequests
					.createContactEventRequest(caseId, checkCodeName, checkCodeBirthday, startDate, Option.none(), irisUserId, sormasUserId);

				var now = Instant.now();
				var irisMessage = createNoteTextForIrisRequest(dataRequest, now);

				task.setStatusChangeDate(now);
				task.setTaskStatus(TaskStatus.DONE);
				task.setAssigneeReply(joinWith("\n\n", task.getAssigneeReply(), irisMessage).strip());
				taskControllerApi.postTasks(List.of(task));

				sormasCaseApi.postCases(List.of(caseDto));

				if (StringUtils.isNoneBlank(person.getEmailAddress())) {

					log.info("With Mail");

				} else {

					var newTask = new TaskDto();

					newTask.setUuid(SormasRefId.of().toString());

					newTask.setCreationDate(now);
					newTask.setChangeDate(now);
					newTask.setStatusChangeDate(now);

					var nowDate = ZonedDateTime.now().withSecond(0);
					newTask.setDueDate(nowDate.toInstant());
					newTask.setPerceivedStart(nowDate.plusDays(1).toInstant());

					newTask.setTaskStatus(TaskStatus.PENDING);
					newTask.setAssigneeUser(task.getCreatorUser());
					newTask.setCreatorUser(task.getAssigneeUser());
					newTask.setCreatorComment(
						String.format(
							"IRIS-Code: %s\nBitte tragen Sie die E-Mail-Adresse für den Fall nach oder übermitteln Sie den IRIS-Code per Telefon!",
							dataRequest.getId().toString()));
					newTask.setTaskType(TaskType.CASE_MANAGEMENT);

					newTask.setCaze(task.getCaze());
					newTask.setTaskContext(task.getTaskContext());
					newTask.setPriority(TaskPriority.NORMAL);

//					taskControllerApi.postTasks(List.of(newTask));

					log.info("Without Mail! Timestamp: {}", task.getChangeDate().toEpochMilli());
				}
			}
		}
	}

	private String createNoteTextForIrisRequest(DataRequest dataRequest, Instant now) {
		return String.format(
			"Kontaktnachverfolgung über IRIS gestartet am %s\nIRIS-Code: %s\nTelecode: %s\nPrüfcode: %s",
			now.atZone(ZoneId.systemDefault()).format(ofLocalizedDateTime(MEDIUM)),
			dataRequest.getId(),
			dataRequest.getTeleCode(),
			defaultString(dataRequest.getCheckCodeRandom()));
	}

	private Option<String> calculateBirthdayCheckCodes(PersonDto person) {

		if (person.getBirthdateYYYY() != null && person.getBirthdateMM() != null && person.getBirthdateDD() != null) {

			var date = person.getBirthdateYYYY() * 10000 + person.getBirthdateMM() * 100 + person.getBirthdateDD();
			var dateHash = DigestUtils.md5Hex(Integer.toString(date));

			return Option.of(dateHash);
		}

		return Option.none();
	}

	private String calculateNameCheckCodes(PersonDto person) {

		var firstName = defaultString(person.getFirstName());
		var lastName = defaultString(person.getLastName());

		var name = (firstName + lastName).toLowerCase().replaceAll("[^\\pL\\pN]", "");

		return DigestUtils.md5Hex(name);
	}

	private Option<Instant> positivSampleDate(CaseDataDto caseDto) {

		var samples = sampleControllerApi.getByCaseUuids(List.of(caseDto.getUuid()));

		var sampleDate = samples.stream()
			.filter(it -> it.getPathogenTestResult() == PathogenTestResultType.POSITIVE)
			.sorted(Comparator.comparing(SampleDto::getSampleDateTime))
			.findFirst()
			.map(SampleDto::getSampleDateTime);
//				.map(it -> it.atZone(ZoneId.systemDefault()));

		return Option.ofOptional(sampleDate);
	}

	private Option<Instant> firstRelevantSymptomDate(CaseDataDto caseDto) {

		if (caseDto.getSymptoms().isSymptomatic()) {
			return Option.of(caseDto.getSymptoms().getOnsetDate()/* .atZone(ZoneId.systemDefault()) */);
		}

		return Option.none();
	}

	private boolean isRelevant(TaskDto it) {

		return it.getTaskType() == TaskType.CONTACT_TRACING
			&& it.getTaskStatus() == TaskStatus.PENDING
			&& it.getAssigneeUser().getCaption().contains("IRIS");
	}
}
