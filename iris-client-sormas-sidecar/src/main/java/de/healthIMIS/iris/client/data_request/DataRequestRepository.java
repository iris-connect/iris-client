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
package de.healthIMIS.iris.client.data_request;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.healthIMIS.iris.client.data_request.DataRequest.DataRequestIdentifier;

/**
 * @author Jens Kutzsche
 */
interface DataRequestRepository extends CrudRepository<DataRequest, DataRequestIdentifier> {

	@Query("select count(1) = 0 from DataRequest r where r.teleCode = :teleCode")
	boolean isTeleCodeAvailable(String teleCode);
}
