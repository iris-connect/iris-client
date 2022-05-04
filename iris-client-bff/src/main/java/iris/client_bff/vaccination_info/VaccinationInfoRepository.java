package iris.client_bff.vaccination_info;

import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
public interface VaccinationInfoRepository extends JpaRepository<VaccinationInfo, VaccinationInfoIdentifier> {

	/**
	 * Returns the {@link VaccinationInfo}s created before the given {@link Instant}.
	 *
	 * @param refDate must not be {@literal null}.
	 * @return
	 */
	Streamable<VaccinationInfo> findByMetadataCreatedIsBefore(Instant refDate);

	@Query("select count(v)>0 from VaccinationInfo v where v.metadata.createdBy = :userId OR v.metadata.lastModifiedBy = :userId")
	boolean isReferencedToUser(UUID userId);
}
