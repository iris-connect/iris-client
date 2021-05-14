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

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "case_data_request")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CaseDataRequest extends Aggregate<CaseDataRequest, CaseDataRequest.DataRequestIdentifier> {

  {
	id = DataRequestIdentifier.of(UUID.randomUUID());
  }

  private @Setter String refId;
  private String hdUserId;

  private @Setter String name;
  private Instant requestStart;
  private Instant requestEnd;
  private @Setter String comment;

  @Column(nullable = false) @Enumerated(EnumType.STRING)
  private Status status = Status.DATA_REQUESTED;

  @Builder
  public CaseDataRequest(String refId, String name, Instant requestStart, Instant requestEnd,
	  String hdUserId, String comment) {

	super();

	id = DataRequestIdentifier.of(UUID.randomUUID());

	this.refId = refId;
	this.name = name;
	this.requestStart = requestStart;
	this.requestEnd = requestEnd;
	this.hdUserId = hdUserId;
	this.comment = comment;
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
