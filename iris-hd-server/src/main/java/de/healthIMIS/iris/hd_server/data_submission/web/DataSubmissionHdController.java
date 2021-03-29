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
package de.healthIMIS.iris.hd_server.data_submission.web;

import de.healthIMIS.iris.hd_server.core.DepartmentIdentifier;
import de.healthIMIS.iris.hd_server.data_submission.DataSubmission;
import de.healthIMIS.iris.hd_server.data_submission.DataSubmission.Feature;
import de.healthIMIS.iris.hd_server.data_submission.DataSubmissionRepository;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of the internal end-points for health department site to exchange data submissions.
 * 
 * @author Jens Kutzsche
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class DataSubmissionHdController {

	private final @NonNull DataSubmissionRepository submissions;

	@GetMapping("/hd/data-submissions")
	List<DataSubmissionClientOutputDto> getDataSubmissions(
			@RequestParam("departmentId") DepartmentIdentifier departmentId) {

		var dataSubmissions = submissions.findAllByDepartmentId(departmentId);

		submissions.deleteAll(dataSubmissions.toList());

		var dtos = dataSubmissions.stream().map(it -> DataSubmissionClientOutputDto.of(it)).collect(Collectors.toList());

		log.debug("Submission - GET from hd client: {}",
				dtos.stream().map(DataSubmissionClientOutputDto::getRequestId).collect(Collectors.joining(", ")));

		return dtos;
	}

	@Data
	static class DataSubmissionClientOutputDto {

		static DataSubmissionClientOutputDto of(DataSubmission submission) {
			return new DataSubmissionClientOutputDto(submission.getId().toString(), submission.getRequestId().toString(),
					submission.getDepartmentId().toString(), submission.getSalt(), submission.getKeyReference(),
					submission.getEncryptedData(), submission.getFeature());
		}

		private final String id;

		private final String requestId;

		private final String departmentId;

		private final String salt;

		private final String keyReference;

		private final String encryptedData;

		private final Feature feature;
	}
}
