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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CaseDataSubmission extends Aggregate<CaseDataSubmission, CaseDataSubmission.DataSubmissionIdentifier> {

	@ManyToOne //
	@JoinColumn(name = "request_id")
	private final CaseDataRequest request;

	@Embedded
	private CaseDataProvider dataProvider;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "submission_id")
	private Set<Contact> contacts;

	private Instant contactsStartDate;
	private Instant contactsEndDate;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "submission_id")
	private Set<CaseEvent> events;

	private Instant eventsStartDate;
	private Instant eventsEndDate;

	public CaseDataSubmission(CaseDataRequest request,
							  Set<Contact> contacts, Instant contactsStartDate, Instant contactsEndDate,
							  Set<CaseEvent> events, Instant eventsStartDate, Instant eventsEndDate,
							  CaseDataProvider dataProvider) {

		super();

		id = DataSubmissionIdentifier.random();
		this.request = request;
		this.contacts = contacts;
		this.contactsStartDate = contactsStartDate;
		this.contactsEndDate = contactsEndDate;
		this.events = events;
		this.eventsStartDate = eventsStartDate;
		this.eventsEndDate = eventsEndDate;
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

	public LocalDate getEventsStartDateAsLocalDate() {
		return safelyParseInstant(eventsStartDate);
	}

	public LocalDate getEventsEndDateAsLocalDate() {
		return safelyParseInstant(eventsEndDate);
	}

	public LocalDate getContactsStartDateAsLocalDate() {
		return safelyParseInstant(contactsStartDate);
	}

	public LocalDate getContactsEndDateAsLocalDate() {
		return safelyParseInstant(contactsEndDate);
	}

	private LocalDate safelyParseInstant(Instant instant) {
		return instant == null ? null : LocalDate.ofInstant(instant, ZoneId.of("CET"));
	}
}
