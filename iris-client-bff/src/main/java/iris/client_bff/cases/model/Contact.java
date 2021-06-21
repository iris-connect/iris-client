package iris.client_bff.cases.model;

import iris.client_bff.core.Sex;
import iris.client_bff.events.model.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Contact {

	@Id
	private UUID contactId = UUID.randomUUID();

	@ManyToOne //
	@JoinColumn(name = "submission_id")
	private CaseDataSubmission submission;

	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;

	@Enumerated(EnumType.STRING)
	private Sex sex = Sex.UNKNOWN;

	private String email;
	private String phone;
	private String mobilePhone;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address workplaceAddress;

	private String workplaceName;
	private String workplacePointOfContact;
	private LocalDate firstContactDate;
	private LocalDate lastContactDate;
	private String contactCategory;
	private String basicConditions;
}
