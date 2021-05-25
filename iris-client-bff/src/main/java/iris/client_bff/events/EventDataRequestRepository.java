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
package iris.client_bff.events;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest.Status;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Jens Kutzsche
 */
public interface EventDataRequestRepository extends JpaRepository<EventDataRequest, DataRequestIdentifier> {

	@Query("select count(1) = 0 from EventDataRequest r where r.id = :code")
	boolean isCodeAvailable(UUID code);

	@Query("select count(r) from EventDataRequest r where r.metadata.created >= :date")
	int getCountSinceDate(Instant date);

	@Query("select count(r) from EventDataRequest r where r.status = :status")
	int getCountWithStatus(Status status);

	Page<EventDataRequest> findByStatus(Status status, Pageable pageable);

	Page<EventDataRequest> findByRefIdContainsOrNameContainsAllIgnoreCase(String search, String search1, Pageable pageable);

	@Query("select r from EventDataRequest r where r.status = :status and ( upper(r.refId) like concat('%', upper(:search), '%') or upper(r.name) like concat('%', upper(:search), '%'))")
	Page<EventDataRequest> findByStatusAndSearchByRefIdOrName(Status status, String search, Pageable pageable);
}
