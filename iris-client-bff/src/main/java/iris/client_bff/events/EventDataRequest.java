package iris.client_bff.events;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.IdWithUuid;
import iris.client_bff.events.model.Location;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

/**
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "event_data_request")
@Indexed
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EventDataRequest extends Aggregate<EventDataRequest, EventDataRequest.DataRequestIdentifier> {

	{
		id = DataRequestIdentifier.of(UUID.randomUUID());
	}

	@KeywordField(sortable = Sortable.YES, normalizer = "german")
	private @Setter String refId;
	private String hdUserId;

	@KeywordField(sortable = Sortable.YES, normalizer = "german")
	private @Setter String name;
	private @Setter String comment;

	@GenericField(sortable = Sortable.YES)
	private Instant requestStart;

	@GenericField(sortable = Sortable.YES)
	private Instant requestEnd;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@GenericField(sortable = Sortable.YES)
	private Status status = Status.DATA_REQUESTED;

	private String requestDetails = null;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "location_id")
	@IndexedEmbedded
	private Location location;

	private String announcementToken;

	@Builder
	public EventDataRequest(String refId, String name, Instant requestStart, Instant requestEnd, String comment,
			String requestDetails, String hdUserId, Location location, String announcementToken) {

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

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class DataRequestIdentifier extends IdWithUuid {

		private static final long serialVersionUID = -7069968999070254963L;

		final UUID requestId;

		@Override
		protected UUID getBasicId() {
			return requestId;
		}
	}

	public enum Status {
		DATA_REQUESTED, DATA_RECEIVED, CLOSED, ABORTED
	}
}
