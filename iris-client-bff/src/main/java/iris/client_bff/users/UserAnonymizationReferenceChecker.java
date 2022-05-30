package iris.client_bff.users;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Predicate} to check if a {@link UserAccount} can be anonymized. Modules with entities that reference users
 * and need the user data for legal reasons even after the user has been deleted must provide an implementation of this
 * interface. Their implementations are used together to verify that anonymization can be performed.
 *
 * @author Jens Kutzsche
 */
public interface UserAnonymizationReferenceChecker extends Predicate<UserAccount> {

	@Override
	default boolean test(UserAccount user) {
		return canUserBeAnonymized(user);
	}

	/**
	 * Shall check if from the module's point of view a user can be anonymized or if references of the contained entities
	 * to the user prohibit this.
	 * <p />
	 * The check should be performed only for entities that, for legal reasons, need the user data for some time beyond
	 * the deletion of the user.
	 *
	 * @param user The {@link UserAccount} to be anonymized.
	 * @return True, if anonymization can be performed.
	 */
	boolean canUserBeAnonymized(UserAccount user);

	default UserAnonymizationReferenceChecker and(UserAnonymizationReferenceChecker other) {

		Objects.requireNonNull(other);
		return it -> test(it) && other.test(it);
	}
}
