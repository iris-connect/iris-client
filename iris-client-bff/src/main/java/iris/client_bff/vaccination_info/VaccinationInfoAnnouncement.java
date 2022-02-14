package iris.client_bff.vaccination_info;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.IdWithUuid;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // for JPA
	public static class AnnouncementIdentifier extends IdWithUuid {

		private static final long serialVersionUID = 6389647206633809409L;

		final UUID id;

		static AnnouncementIdentifier random() {
			return of(UUID.randomUUID());
		}

		@Override
		protected UUID getBasicId() {
			return id;
		}
	}
}
