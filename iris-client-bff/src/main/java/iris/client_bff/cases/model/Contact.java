package iris.client_bff.cases.model;

import iris.client_bff.core.Sex;
import iris.client_bff.core.model.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	@Embedded
	private Address address;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "street", column = @Column(name = "workplace_street")),
			@AttributeOverride(name = "houseNumber", column = @Column(name = "workplace_house_number")),
			@AttributeOverride(name = "zipCode", column = @Column(name = "workplace_zip_code")),
			@AttributeOverride(name = "city", column = @Column(name = "workplace_city"))
	})
	private Address workplaceAddress;

	private String workplaceName;
	private String workplacePhone;
	private String workplacePointOfContact;
	private LocalDate firstContactDate;
	private LocalDate lastContactDate;

	@Enumerated(EnumType.STRING)
	private ContactCategory contactCategory;

	private String basicConditions;

	public enum ContactCategory {
		HIGH_RISK, HIGH_RISK_MED, MEDIUM_RISK_MED, LOW_RISK, NO_RISK
	}
}
