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
package iris.client_bff.data_request.events;

import iris.client_bff.data_request.DataRequest;
import java.time.Instant;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jens Kutzsche
 */
@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("Event")
public class EventDataRequest extends DataRequest {

	private String requestDetails = null;

	@OneToOne(orphanRemoval = true, cascade = { CascadeType.ALL })
	@JoinColumn(name = "location_id")
	private Location location;

	@Builder
	public EventDataRequest(String refId, String name, Instant requestStart, Instant requestEnd, String comment,
			String requestDetails, String hdUserId, Location location, Set<DataRequest.Feature> features) {

		super(refId, name, requestStart, requestEnd, comment, hdUserId, features);

		this.requestDetails = requestDetails;

		this.location = location;
	}
}
