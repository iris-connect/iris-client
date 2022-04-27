package iris.client_bff.vaccination_info;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserAnonymizationReferenceChecker;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
class VaccinationInfoToUserReferenceChecker implements UserAnonymizationReferenceChecker {

	private final VaccinationInfoRepository vaccRepo;
	private final VaccinationInfoAnnouncementRepository announcementRepo;

	@Override
	public boolean canUserBeAnonymized(UserAccount user) {
		return !vaccRepo.isReferencedToUser(user.getId().toUuid())
				&& !announcementRepo.isReferencedToUser(user.getId().toUuid());
	}
}
