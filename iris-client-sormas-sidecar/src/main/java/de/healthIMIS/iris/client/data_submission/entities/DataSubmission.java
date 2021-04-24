package de.healthIMIS.iris.client.data_submission.entities;

import de.healthIMIS.iris.client.core.Aggregate;
import de.healthIMIS.iris.client.core.Id;
import de.healthIMIS.iris.client.data_request.DataRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
// @Table(name = "data_request")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class DataSubmission extends Aggregate<DataSubmission, DataSubmission.DataSubmissionIdentifier> {

	@ManyToOne //
	@JoinColumn(name = "request_id")
	private final DataRequest request;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //
	@JoinColumn(name = "submission_id")
	private Set<Guest> guests;

	@Embedded
	private GuestListDataProvider dataProvider;
	private String additionalInformation;
	private Instant startDate;
	private Instant endDate;

	public DataSubmission(DataRequest request, Set<Guest> guests, GuestListDataProvider dataProvider,
			String additionalInformation,
			Instant startDate, Instant endDate) {

		super();

		id = DataSubmissionIdentifier.random();
		this.request = request;
		this.guests = guests;
		this.dataProvider = dataProvider;
		this.additionalInformation = additionalInformation;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class DataSubmissionIdentifier implements Id, Serializable {

		private static final long serialVersionUID = -8254677010830428881L;

		final UUID submissionId;

		/**
		 * for JSON deserialization
		 */
		public static DataSubmissionIdentifier random() {
			return of(UUID.randomUUID());
		}

		@Override
		public String toString() {
			return submissionId.toString();
		}
	}
}
