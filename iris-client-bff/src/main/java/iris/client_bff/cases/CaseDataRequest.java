package iris.client_bff.cases;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import iris.client_bff.core.token.IdentifierToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

/**
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "case_data_request")
@Indexed
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CaseDataRequest extends Aggregate<CaseDataRequest, CaseDataRequest.DataRequestIdentifier> {

	{
		id = DataRequestIdentifier.of(UUID.randomUUID());
	}

	@KeywordField(sortable = Sortable.YES, normalizer = "german")
	private @Setter String refId;
	private String hdUserId;

	@KeywordField(sortable = Sortable.YES, normalizer = "german")
	private @Setter String name;

	@GenericField(sortable = Sortable.YES)
	private Instant requestStart;

	@GenericField(sortable = Sortable.YES)
	private Instant requestEnd;

	private @Setter String comment;
	private String announcementToken;
	private String dataAuthorizationToken;
	private String readableToken;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@GenericField(sortable = Sortable.YES)
	private Status status = Status.DATA_REQUESTED;

	@Builder
	public CaseDataRequest(String refId, String name, Instant requestStart, Instant requestEnd,
			String hdUserId, String comment, @NonNull IdentifierToken identifierToken, String announcementToken) {

		id = DataRequestIdentifier.of(UUID.randomUUID());

		this.refId = refId;
		this.name = name;
		this.requestStart = requestStart;
		this.requestEnd = requestEnd;
		this.hdUserId = hdUserId;
		this.comment = comment;
		this.dataAuthorizationToken = identifierToken.dataAuthorizationToken();
		this.readableToken = identifierToken.readableToken();
		this.announcementToken = announcementToken;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class DataRequestIdentifier implements Id, Serializable {

		private static final long serialVersionUID = -8254677010830428881L;

		@Getter
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
