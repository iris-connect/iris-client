package iris.client_bff.users;

import lombok.NonNull;

import java.util.Arrays;

public enum UserRole {

	ADMIN, USER, DELETED, ANONYMOUS;

	public static boolean isUserRole(@NonNull String candidate) {

		return Arrays.stream(UserRole.values())
				.map(UserRole::name)
				.anyMatch(candidate::equals);
	}
}
