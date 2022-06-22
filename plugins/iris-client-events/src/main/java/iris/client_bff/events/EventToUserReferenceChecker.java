package iris.client_bff.events;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserAnonymizationReferenceChecker;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
class EventToUserReferenceChecker implements UserAnonymizationReferenceChecker {

	private final EventDataRequestRepository eventRepo;
	private final EventDataSubmissionRepository submissionRepo;

	@Override
	public boolean canUserBeAnonymized(UserAccount user) {
		return !eventRepo.isReferencedToUser(user.getId().toUuid())
				&& !submissionRepo.isReferencedToUser(user.getId().toUuid());
	}
}
