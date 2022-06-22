package iris.client_bff.events.model;

import iris.client_bff.core.model.Aggregate;
import iris.client_bff.core.model.IdWithUuid;
import iris.client_bff.events.EventDataRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class EventDataSubmission extends Aggregate<EventDataSubmission, EventDataSubmission.DataSubmissionIdentifier> {

	@ManyToOne //
	@JoinColumn(name = "request_id")
	private final EventDataRequest request;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //
	@JoinColumn(name = "submission_id")
	private Set<Guest> guests;

	@Embedded
	private GuestListDataProvider dataProvider;
	private String additionalInformation;
	private Instant startDate;
	private Instant endDate;

	public EventDataSubmission(EventDataRequest request, Set<Guest> guests, GuestListDataProvider dataProvider,
			String additionalInformation,
			Instant startDate, Instant endDate) {

		id = DataSubmissionIdentifier.random();
		this.request = request;
		this.guests = guests;
		this.dataProvider = dataProvider;
		this.additionalInformation = additionalInformation;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // for JPA
	public static class DataSubmissionIdentifier extends IdWithUuid {

		private static final long serialVersionUID = -3879046114187116862L;

		final UUID submissionId;

		public static DataSubmissionIdentifier random() {
			return of(UUID.randomUUID());
		}

		@Override
		protected UUID getBasicId() {
			return submissionId;
		}
	}
}
