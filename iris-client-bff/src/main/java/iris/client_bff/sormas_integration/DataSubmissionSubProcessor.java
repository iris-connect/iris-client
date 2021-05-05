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
package iris.client_bff.sormas_integration;

import iris.client_bff.core.SormasRefId;
import iris.client_bff.data_request.DataRequest;
import iris.sormas.client.api.TaskControllerApi;
import iris.sormas.client.model.TaskDto;
import iris.sormas.client.model.TaskStatus;
import iris.sormas.client.model.UserReferenceDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author Jens Kutzsche
 */
@RequiredArgsConstructor
@Getter
public abstract class DataSubmissionSubProcessor<T> {

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

		var nowDate = Instant.now().truncatedTo(ChronoUnit.MINUTES);
		newTask.setDueDate(nowDate);
		newTask.setPerceivedStart(nowDate.plus(1, ChronoUnit.DAYS));

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
