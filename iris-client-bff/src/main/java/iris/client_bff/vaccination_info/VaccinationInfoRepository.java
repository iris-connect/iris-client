package iris.client_bff.vaccination_info;

import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jens Kutzsche
 */
public interface VaccinationInfoRepository extends JpaRepository<VaccinationInfo, VaccinationInfoIdentifier> {}
