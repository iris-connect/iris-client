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
package iris.client_bff.cases;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Jens Kutzsche
 */
public interface CaseDataRequestRepository extends CrudRepository<CaseDataRequest, DataRequestIdentifier> {

  @Query("select count(1) = 0 from CaseDataRequest r where r.id = :code")
  boolean isCodeAvailable(UUID code);

	@Query("select count(r) from CaseDataRequest r where r.metadata.created >= :date")
	int getCountSinceDate(Instant date);

	@Query("select count(r) from CaseDataRequest r where r.status = :status")
	int getCountWithStatus(Status status);

}
