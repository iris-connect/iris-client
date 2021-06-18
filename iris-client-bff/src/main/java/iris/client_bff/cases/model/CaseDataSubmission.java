package iris.client_bff.cases.model;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CaseDataSubmission extends Aggregate<CaseDataSubmission, CaseDataSubmission.DataSubmissionIdentifier> {

	@ManyToOne //
	@JoinColumn(name = "request_id")
	private final CaseDataRequest request;

	@Embedded
	private CaseDataProvider dataProvider;

	// TODO: add contacts, events
	// @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //
	// @JoinColumn(name = "submission_id")
	// private Set<Guest> guests;

	public CaseDataSubmission(CaseDataRequest request, CaseDataProvider dataProvider) {// , Set<?> contactPersons, Set<?> events) {

		super();

		id = DataSubmissionIdentifier.random();
		this.request = request;
		// this.guests = guests;
		this.dataProvider = dataProvider;
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
