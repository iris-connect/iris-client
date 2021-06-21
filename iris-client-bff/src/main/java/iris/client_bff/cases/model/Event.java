package iris.client_bff.cases.model;

import iris.client_bff.events.model.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Event {

	@Id
	private UUID eventId = UUID.randomUUID();

	@ManyToOne //
	@JoinColumn(name = "submission_id")
	private CaseDataSubmission submission;

	private String name;
	private String phone;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;

	private String additionalInformation;
}
