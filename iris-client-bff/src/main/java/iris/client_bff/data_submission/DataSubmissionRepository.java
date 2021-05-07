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
package iris.client_bff.data_submission;

import iris.client_bff.data_request.events.EventDataRequest;
import iris.client_bff.data_submission.model.DataSubmission;
import iris.client_bff.data_submission.model.DataSubmission.DataSubmissionIdentifier;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
public interface DataSubmissionRepository extends CrudRepository<DataSubmission, DataSubmissionIdentifier> {

	@Transactional
	Streamable<DataSubmission> findAllByRequest(EventDataRequest request);
}
