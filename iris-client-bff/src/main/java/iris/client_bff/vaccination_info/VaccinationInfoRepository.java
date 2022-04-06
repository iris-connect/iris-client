package iris.client_bff.vaccination_info;

import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
