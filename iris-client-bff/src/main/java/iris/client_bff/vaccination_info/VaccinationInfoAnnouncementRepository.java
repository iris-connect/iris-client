package iris.client_bff.vaccination_info;

import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
public interface VaccinationInfoAnnouncementRepository
		extends JpaRepository<VaccinationInfoAnnouncement, AnnouncementIdentifier> {

	/**
	 * Returns the {@link VaccinationInfoAnnouncement}s created before the given {@link Instant}.
	 *
	 * @param refDate must not be {@literal null}.
	 * @return
	 */
	Streamable<VaccinationInfoAnnouncement> findByMetadataCreatedIsBefore(Instant refDate);
}
