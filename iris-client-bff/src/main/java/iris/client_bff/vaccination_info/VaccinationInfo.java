package iris.client_bff.vaccination_info;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Jens Kutzsche
 */
@Entity
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
	private String announcementToken;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

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
		static VaccinationInfoIdentifier of(String uuid) {
			return of(UUID.fromString(uuid));
		}

		@Override
		public String toString() {
			return id.toString();
		}
	}

	public enum Status {
		ANNOUNCED
	}
}
