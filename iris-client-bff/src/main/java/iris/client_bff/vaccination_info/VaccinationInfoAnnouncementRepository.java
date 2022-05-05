package iris.client_bff.vaccination_info;

import iris.client_bff.core.AggregateRepository;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;

/**
 * @author Jens Kutzsche
 */
public interface VaccinationInfoAnnouncementRepository
		extends AggregateRepository<VaccinationInfoAnnouncement, AnnouncementIdentifier> {}
