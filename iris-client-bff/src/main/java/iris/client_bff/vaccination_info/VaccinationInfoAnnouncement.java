package iris.client_bff.vaccination_info;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;
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

import javax.persistence.Embeddable;
import javax.persistence.Entity;

/**
 * @author Jens Kutzsche
 */
@Entity
@Data
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true, of = {})
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VaccinationInfoAnnouncement extends Aggregate<VaccinationInfoAnnouncement, AnnouncementIdentifier> {

	{
		id = AnnouncementIdentifier.random();
	}

	private String externalId;
	private String announcementToken;

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class AnnouncementIdentifier implements Id, Serializable {

		private static final long serialVersionUID = 6389647206633809409L;

		@Getter
		final UUID id;

		static AnnouncementIdentifier random() {
			return of(UUID.randomUUID());
		}

		/**
		 * for JSON deserialization
		 */
		static AnnouncementIdentifier of(String uuid) {
			return of(UUID.fromString(uuid));
		}

		@Override
		public String toString() {
			return id.toString();
		}
	}
}
