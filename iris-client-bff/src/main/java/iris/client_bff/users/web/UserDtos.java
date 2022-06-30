package iris.client_bff.users.web;

import iris.client_bff.core.validation.AttackDetector.Password;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.core.validation.ValidPassword;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Jens Kutzsche
 */
interface UserDtos {

	@Builder
	record Output(
			String id,
			String firstName,
			String lastName,
			String userName,
			Role role,
			boolean locked,
			boolean useMfa,
			boolean mfaSecretEnrolled,

			Instant createdAt,
			Instant lastModifiedAt,
			String createdBy,
			String lastModifiedBy) {}

	record OutputList(@JsonProperty("users") List<Output> userDTOs) {}

	record Insert(

			@Nullable @Size(max = 200) @NoSignOfAttack String firstName,

			@Nullable @Size(max = 200) @NoSignOfAttack String lastName,

			@NotBlank @Size(max = 50) @NoSignOfAttack String userName,

			@ValidPassword @NotBlank @Size(max = 200) @NoSignOfAttack(payload = Password.class) String password,

			@NotNull Role role,

			boolean locked,

			boolean useMfa) {}

	@Builder
	record Update(

			@Nullable @Size(max = 200) @NoSignOfAttack String firstName,

			@Nullable @Size(max = 200) @NoSignOfAttack String lastName,

			@Nullable @Size(max = 50) @NoSignOfAttack String userName,

			@Nullable @ValidPassword @Size(max = 200) @NoSignOfAttack(payload = Password.class) String password,

			@Nullable @Size(max = 200) @NoSignOfAttack(payload = Password.class) String oldPassword,

			@Nullable Role role,

			@Nullable Boolean locked,

			@Nullable Boolean useMfa) {}

	enum Role {
		ADMIN, USER, DELETED, ANONYMOUS
	}
}
