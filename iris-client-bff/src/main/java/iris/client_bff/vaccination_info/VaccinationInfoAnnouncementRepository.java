package iris.client_bff.vaccination_info;

import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

	@Query("select count(a)>0 from VaccinationInfoAnnouncement a where a.metadata.createdBy = :userId OR a.metadata.lastModifiedBy = :userId")
	boolean isReferencedToUser(UUID userId);
}
