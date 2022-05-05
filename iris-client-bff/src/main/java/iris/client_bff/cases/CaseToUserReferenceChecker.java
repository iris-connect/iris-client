package iris.client_bff.cases;

import iris.client_bff.users.UserAnonymizationReferenceChecker;
import iris.client_bff.users.entities.UserAccount;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
class CaseToUserReferenceChecker implements UserAnonymizationReferenceChecker {

	private final CaseDataRequestRepository caseRepo;
	private final CaseDataSubmissionRepository submissionRepo;

	@Override
	public boolean canUserBeAnonymized(UserAccount user) {
		return !caseRepo.isReferencedToUser(user.getId().toUuid())
				&& !submissionRepo.isReferencedToUser(user.getId().toUuid());
	}
}
