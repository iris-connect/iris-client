package iris.client_bff.vaccination_info;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import iris.client_bff.core.Sex;
import iris.client_bff.core.model.Address;
import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@AllArgsConstructor(staticName = "of")
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

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class VaccinationInfoIdentifier implements Id, Serializable {

		private static final long serialVersionUID = 6389647206633809409L;

		@Getter
		final UUID id;

		static VaccinationInfoIdentifier random() {
			return of(UUID.randomUUID());
		}

		/**
		 * for JSON deserialization
		 */
		public static VaccinationInfoIdentifier of(String uuid) {
			return of(UUID.fromString(uuid));
		}

		@Override
		public String toString() {
			return id.toString();
		}
	}

	@Embeddable
	@Data
	@Setter(AccessLevel.PACKAGE)
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
