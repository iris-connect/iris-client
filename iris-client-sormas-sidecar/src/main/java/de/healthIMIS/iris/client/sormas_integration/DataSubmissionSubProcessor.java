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
import de.healthIMIS.sormas.client.api.TaskControllerApi;
import de.healthIMIS.sormas.client.model.TaskDto;
import de.healthIMIS.sormas.client.model.TaskStatus;
import de.healthIMIS.sormas.client.model.UserReferenceDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author Jens Kutzsche
 */
@RequiredArgsConstructor
@Getter
abstract class DataSubmissionSubProcessor<T> {

	protected final DataRequest request;
	protected final TaskControllerApi taskApi;

	public abstract void process(T dto);

	protected void createAndTransmitTask(UnaryOperator<TaskDto> customizer) {

		var newTask = createTask();

		newTask = customizer.apply(newTask);

		System.out.println(taskApi.postTasks(List.of(newTask)));
	}

	protected TaskDto createTask() {
		var newTask = new TaskDto();

		newTask.setUuid(SormasRefId.random().toString());

		var nowDate = ZonedDateTime.now().withSecond(0);
		newTask.setDueDate(nowDate.toInstant());
		newTask.setPerceivedStart(nowDate.plusDays(1).toInstant());

		newTask.setTaskStatus(TaskStatus.PENDING);

		var userRef = new UserReferenceDto();
		// ToDo: TBD with Jens
		// userRef.setUuid(request.getSormasUserId());
		newTask.setAssigneeUser(userRef);

		userRef = new UserReferenceDto();
		// userRef.setUuid(request.getIrisUserId());
		newTask.setCreatorUser(userRef);
		return newTask;
	}
}
