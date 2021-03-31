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

import static java.time.format.DateTimeFormatter.*;
import static java.time.format.FormatStyle.*;
import static org.apache.commons.lang3.StringUtils.*;

import de.healthIMIS.iris.client.core.SormasRefId;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.sormas_integration.SyncTimes.DataTypes;
import de.healthIMIS.sormas.client.api.CaseControllerApi;
import de.healthIMIS.sormas.client.api.EventControllerApi;
import de.healthIMIS.sormas.client.api.PersonControllerApi;
import de.healthIMIS.sormas.client.api.SampleControllerApi;
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.CaseDataDto;
import de.healthIMIS.sormas.client.model.PathogenTestResultType;
import de.healthIMIS.sormas.client.model.SampleDto;
import de.healthIMIS.sormas.client.model.TaskContext;
import de.healthIMIS.sormas.client.model.TaskDto;
import de.healthIMIS.sormas.client.model.TaskPriority;
import de.healthIMIS.sormas.client.model.TaskStatus;
import de.healthIMIS.sormas.client.model.TaskType;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

/**
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!inttest")
@ConditionalOnProperty("iris.sormas.user")
class DataRequestJob {

	public static final String MAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	private static final Pattern MAIL_REGEX = Pattern.compile(MAIL_PATTERN, Pattern.CASE_INSENSITIVE);

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull CaseControllerApi sormasCaseApi;
	private final @NonNull PersonControllerApi sormasPersonApi;
	private final @NonNull TaskControllerApi taskControllerApi;
	private final @NonNull EventControllerApi eventControllerApi;
	private final @NonNull SampleControllerApi sampleControllerApi;
	private final @NonNull DataRequestManagement dataRequests;

	private long errorCounter = 0;

	@Scheduled(fixedDelay = 15000)
	void run() {

		log.trace("Request job - start");

		var lastTime = syncTimes.findById(DataTypes.Tasks).map(SyncTimes::getLastSync).map(Instant::toEpochMilli)
				.orElse(0l);

		var tasks = taskControllerApi.getAll7(lastTime);

		try {

			tasks.stream().sorted(Comparator.comparing(TaskDto::getChangeDate)).filter(this::isRelevant).map(this::handleTask)
					.forEach(it -> {

						if (it.getChangeDate() != null) {
							syncTimes.save(SyncTimes.of(DataTypes.Tasks, it.getChangeDate()));
						}
					});

		} catch (RestClientException e) {

			errorCounter++;

			e.printStackTrace();

			if (errorCounter == 1 || errorCounter % 20 == 0) {
				if (log.isTraceEnabled()) {
					log.warn(
							String.format("Request job - can't handle task and create data requests for %s tries!", errorCounter), e);
				} else {
					log.warn("Request job - can't handle task and create data requests for {} tries!", errorCounter);
				}
			}
		}
	}

	private TaskDto handleTask(TaskDto task) {

		switch (task.getTaskContext()) {
			case CASE: {
				handleCaseTask(task);
				break;
			}
			case EVENT: {
				handleEventTask(task);
				break;
			}
			default:
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

				var caseId = caseDto.getUuid();
				var name = caseDto.getPerson().getCaption();
				var sormasUserId = task.getCreatorUser().getUuid();

				var startDate = firstRelevantSymptomDate(caseDto).orElse(positivSampleDate(caseDto)).get();

				var dataRequest = dataRequests.createContactEventRequest(caseId, name,
						startDate, Option.none(), sormasUserId);

				var now = Instant.now();
				var irisMessage = createNoteTextForIrisRequest("Kontaktnachverfolgung", dataRequest, now);

				task.setStatusChangeDate(now);
				task.setTaskStatus(TaskStatus.DONE);
				task.setAssigneeReply(joinWith("\n\n", task.getAssigneeReply(), irisMessage).strip());
				taskControllerApi.postTasks(List.of(task));

				if (StringUtils.isNoneBlank(person.getEmailAddress())) {

					log.info("With Mail");

				} else {

					var newTask = new TaskDto();

					newTask.setUuid(SormasRefId.random().toString());

					newTask.setCreationDate(now);
					newTask.setChangeDate(now);
					newTask.setStatusChangeDate(now);

					var nowDate = ZonedDateTime.now().withSecond(0);
					newTask.setDueDate(nowDate.toInstant());
					newTask.setPerceivedStart(nowDate.plusDays(1).toInstant());

					newTask.setTaskStatus(TaskStatus.PENDING);
					newTask.setAssigneeUser(task.getCreatorUser());
					newTask.setCreatorUser(task.getAssigneeUser());
					newTask.setCreatorComment(String.format(
							"IRIS-Code: %s\nBitte tragen Sie die E-Mail-Adresse für den Fall nach oder übermitteln Sie den IRIS-Code per Telefon!",
							dataRequest.getId().toString()));
					newTask.setTaskType(TaskType.CASE_MANAGEMENT);

					newTask.setCaze(task.getCaze());
					newTask.setTaskContext(task.getTaskContext());
					newTask.setPriority(TaskPriority.NORMAL);

					// taskControllerApi.postTasks(List.of(newTask));

					log.info("Without Mail! Timestamp: {}", task.getChangeDate().toEpochMilli());
				}
			}
		}
	}

	private void handleEventTask(TaskDto task) {

		var events = eventControllerApi.getByUuids14(List.of(task.getEvent().getUuid()));

		if (!events.isEmpty()) {

			var eventDto = events.get(0);

			var eventId = eventDto.getUuid();
			var name = eventDto.getEventTitle();
			var sormasUserId = task.getCreatorUser().getUuid();

			var startDate = eventDto.getStartDate();
			var endDate = eventDto.getEndDate();

			var requestDetails = task.getCreatorComment();

			var dataRequest = dataRequests.createLocationRequest(eventId, name, startDate,
					Option.of(endDate), Option.of(requestDetails), Option.of(sormasUserId));

			var now = Instant.now();
			var irisMessage = createNoteTextForIrisRequest("Ereignisnachverfolgung", dataRequest, now);

			task.setStatusChangeDate(now);
			task.setTaskStatus(TaskStatus.DONE);
			task.setAssigneeReply(joinWith("\n\n", task.getAssigneeReply(), irisMessage).strip());
			taskControllerApi.postTasks(List.of(task));

			var matcher = MAIL_REGEX.matcher(eventDto.getEventDesc());

			Option<String> email = Option.when(matcher.find(), matcher::group);

			if (email.isDefined()) {

				log.info("With Mail");

			} else {

				log.info("Without Mail! Timestamp: {}", task.getChangeDate().toEpochMilli());

			}
		}
	}

	private String createNoteTextForIrisRequest(String title, DataRequest dataRequest, Instant now) {
		return String.format("%s über IRIS gestartet am %s\nIRIS-Code: %s",
				title, now.atZone(ZoneId.systemDefault()).format(ofLocalizedDateTime(MEDIUM)), dataRequest.getId());
	}

	private Option<Instant> positivSampleDate(CaseDataDto caseDto) {

		var samples = sampleControllerApi.getByCaseUuids(List.of(caseDto.getUuid()));

		var sampleDate = samples.stream().filter(it -> it.getPathogenTestResult() == PathogenTestResultType.POSITIVE)
				.sorted(Comparator.comparing(SampleDto::getSampleDateTime)).findFirst().map(SampleDto::getSampleDateTime);
		// .map(it -> it.atZone(ZoneId.systemDefault()));

		return Option.ofOptional(sampleDate);
	}

	private Option<Instant> firstRelevantSymptomDate(CaseDataDto caseDto) {

		if (caseDto.getSymptoms().isSymptomatic()) {
			return Option.of(caseDto.getSymptoms().getOnsetDate()/* .atZone(ZoneId.systemDefault()) */);
		}

		return Option.none();
	}

	private boolean isRelevant(TaskDto it) {

		return ((it.getTaskContext() == TaskContext.CASE && it.getTaskType() == TaskType.CONTACT_TRACING))
				// ToDo: I need to have a chat with Jens about it.
				// || (it.getTaskContext() == TaskContext.EVENT && it.getTaskType() == TaskType.EVENT_INVESTIGATION))
				&& it.getTaskStatus() == TaskStatus.PENDING && it.getAssigneeUser().getCaption().contains("IRIS");
	}
}
