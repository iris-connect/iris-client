package iris.client_bff.vaccination_info;

import static iris.client_bff.vaccination_info.VaccinationStatus.*;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.IdWithUuid;
import iris.client_bff.core.Sex;
import iris.client_bff.core.model.Address;
import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

/**
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "vaccination_infos")
@Indexed
@Data
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VaccinationInfo extends Aggregate<VaccinationInfo, VaccinationInfoIdentifier> {

	{
		id = VaccinationInfoIdentifier.random();
	}

	private String externalId;

	@IndexedEmbedded
	private Facility facility;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "vaccination_info_id", nullable = false)
	private Set<Employee> employees;

	@IndexedEmbedded
	private final VaccinationStatusCount vaccinationStatusCount = new VaccinationStatusCount();

	public static VaccinationInfo of(String externalId, Facility facility, Set<Employee> createEmployees) {
		return new VaccinationInfo(externalId, facility, createEmployees);
	}

	VaccinationInfo(String externalId, Facility facility, Set<Employee> employees) {

		this.externalId = externalId;
		this.facility = facility;
		this.employees = employees;

		determineStatusCounts();
	}

	@PrePersist
	@PostLoad
	void determineStatusCounts() {
		vaccinationStatusCount.determineCounts(getEmployees());
	}

	void setEmployees(Set<Employee> employees) {

		this.employees = employees;
		determineStatusCounts();
	}

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // for JPA
	public static class VaccinationInfoIdentifier extends IdWithUuid {

		private static final long serialVersionUID = 6389647206633809409L;

		final UUID id;

		static VaccinationInfoIdentifier random() {
			return of(UUID.randomUUID());
		}

		@Override
		protected UUID getBasicId() {
			return id;
		}
	}

	@Embeddable
	@Data
	@Setter(AccessLevel.PACKAGE)
	@AllArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class VaccinationStatusCount {

		@Column(name = "status_vaccinated_count")
		@GenericField(sortable = Sortable.YES)
		private long vaccinated;

		@Column(name = "status_not_vaccinated_count")
		@GenericField(sortable = Sortable.YES)
		private long notVaccinated;

		@Column(name = "status_suspicious_proof_count")
		@GenericField(sortable = Sortable.YES)
		private long suspiciousProof;

		@Column(name = "status_unknown_count")
		@GenericField(sortable = Sortable.YES)
		private long unknown;

		void determineCounts(Set<Employee> employees) {

			var statusCountMap = employees.stream()
					.collect(Collectors.groupingBy(
							VaccinationInfo.Employee::getVaccinationStatus,
							Collectors.counting()));

			vaccinated = statusCountMap.getOrDefault(VACCINATED, 0l);
			notVaccinated = statusCountMap.getOrDefault(NOT_VACCINATED, 0l);
			suspiciousProof = statusCountMap.getOrDefault(SUSPICIOUS_PROOF, 0l);
			unknown = statusCountMap.getOrDefault(UNKNOWN, 0l);
		}
	}

	@Embeddable
	@Data
	@Setter(AccessLevel.PACKAGE)
	@Builder
	@AllArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Facility {

		@Column(name = "facility_name")
		@FullTextField(name = "name_search", analyzer = "german")
		@GenericField(sortable = Sortable.YES)
		private String name;

		@IndexedEmbedded
		private Address address;
		private ContactPerson contactPerson;
	}

	@Embeddable
	@Data
	@Setter(AccessLevel.PACKAGE)
	@Builder
	@AllArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ContactPerson {

		@Column(name = "facility_contact_first_name")
		private String firstName;
		@Column(name = "facility_contact_last_name")
		private String lastName;
		@Column(name = "facility_contact_email")
		private String eMail;
		@Column(name = "facility_contact_phone")
		private String phone;
	}

	@Entity
	@Table(name = "employees")
	@Data
	@Setter(AccessLevel.PACKAGE)
	@Builder
	@AllArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Employee {

		@javax.persistence.Id
		private final UUID id = UUID.randomUUID();

		private String firstName;
		private String lastName;
		private LocalDate dateOfBirth;

		@Enumerated(EnumType.STRING)
		private Sex sex = Sex.UNKNOWN;

		private Address address;
		private String email;
		private String phone;

		@Column(nullable = false)
		@Enumerated(EnumType.STRING)
		@GenericField(sortable = Sortable.YES)
		private VaccinationType vaccination;

		@Column(nullable = false)
		@Enumerated(EnumType.STRING)
		@GenericField(sortable = Sortable.YES)
		private VaccinationStatus vaccinationStatus;
	}

}
