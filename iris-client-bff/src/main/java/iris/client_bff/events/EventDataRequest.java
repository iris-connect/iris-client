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

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import iris.client_bff.events.model.Location;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Jens Kutzsche
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "event_data_request")
@NoArgsConstructor
public class EventDataRequest extends Aggregate<EventDataRequest, EventDataRequest.DataRequestIdentifier> {

  {
	id = DataRequestIdentifier.of(UUID.randomUUID());
  }

  private @Setter String refId;
  private String hdUserId;

  private @Setter String name;
  private @Setter String comment;
  private Instant requestStart;
  private Instant requestEnd;

  @Column(nullable = false) @Enumerated(EnumType.STRING)
  private Status status = Status.DATA_REQUESTED;

  private String requestDetails = null;

  @OneToOne(orphanRemoval = true, cascade = { CascadeType.ALL }) @JoinColumn(name = "location_id")
  private Location location;

  private String announcementToken;

  @Builder
  public EventDataRequest(String refId, String name, Instant requestStart, Instant requestEnd, String comment,
	  String requestDetails, String hdUserId, Location location, String announcementToken) {

	super();

	this.refId = refId;
	this.name = name;
	this.requestStart = requestStart;
	this.requestEnd = requestEnd;
	this.hdUserId = hdUserId;
	this.comment = comment;
	this.requestDetails = requestDetails;
	this.location = location;
	this.announcementToken = announcementToken;
  }

  public Instant getLastModifiedAt() {
	return this.getMetadata().getLastModified();
  }

  public Instant getCreatedAt() {
	return this.getMetadata().getCreated();
  }

  @Embeddable
  @EqualsAndHashCode
  @RequiredArgsConstructor(staticName = "of")
  @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
  public static class DataRequestIdentifier implements Id, Serializable {

	private static final long serialVersionUID = -8254677010830428881L;

	final UUID requestId;

	/**
	 * for JSON deserialization
	 */
	public static DataRequestIdentifier of(String uuid) {
	  return of(UUID.fromString(uuid));
	}

	@Override
	public String toString() {
	  return requestId.toString();
	}
  }

  public enum Status {
	DATA_REQUESTED, DATA_RECEIVED, CLOSED, ABORTED
  }
}
