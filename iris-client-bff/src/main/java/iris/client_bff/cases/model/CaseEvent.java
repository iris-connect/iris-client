package iris.client_bff.cases.model;

import iris.client_bff.core.model.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CaseEvent {

	@Id
	private final UUID eventId = UUID.randomUUID();

	@ManyToOne //
	@JoinColumn(name = "submission_id")
	private CaseDataSubmission submission;

	private String name;
	private String phone;

	@Embedded
	private Address address;

	private String additionalInformation;
}
