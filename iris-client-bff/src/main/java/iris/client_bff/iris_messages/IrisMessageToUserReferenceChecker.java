package iris.client_bff.iris_messages;

import iris.client_bff.users.UserAnonymizationReferenceChecker;
import iris.client_bff.users.entities.UserAccount;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
class IrisMessageToUserReferenceChecker implements UserAnonymizationReferenceChecker {

	private final IrisMessageRepository messageRepo;

	@Override
	public boolean canUserBeAnonymized(UserAccount user) {
		return !messageRepo.isReferencedToUser(user.getId().toUuid());
	}
}
