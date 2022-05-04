package iris.client_bff.users;

import iris.client_bff.users.entities.UserAccount;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Jens Kutzsche
 */
public interface UserAnonymizationReferenceChecker extends Predicate<UserAccount> {

	@Override
	default boolean test(UserAccount user) {
		return canUserBeAnonymized(user);
	}

	boolean canUserBeAnonymized(UserAccount user);

	default UserAnonymizationReferenceChecker and(UserAnonymizationReferenceChecker other) {

		Objects.requireNonNull(other);
		return it -> test(it) && other.test(it);
	}
}
