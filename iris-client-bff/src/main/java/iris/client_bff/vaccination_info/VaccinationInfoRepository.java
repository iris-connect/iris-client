package iris.client_bff.vaccination_info;

import iris.client_bff.core.AggregateRepository;
import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;

/**
 * @author Jens Kutzsche
 */
public interface VaccinationInfoRepository extends AggregateRepository<VaccinationInfo, VaccinationInfoIdentifier> {}
