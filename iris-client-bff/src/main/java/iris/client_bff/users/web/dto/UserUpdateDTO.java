package iris.client_bff.users.web.dto;

import iris.client_bff.core.validation.AttackDetector.Password;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.users.web.ValidPassword;
import lombok.Builder;
import lombok.With;

import javax.annotation.Nullable;
import javax.validation.constraints.Size;

@Builder
public record UserUpdateDTO(

		@Nullable @Size(max = 200) @NoSignOfAttack String firstName,

		@Nullable @Size(max = 200) @NoSignOfAttack String lastName,

		@Nullable @Size(max = 50) @NoSignOfAttack String userName,

		@Nullable @ValidPassword @Size(max = 200) @NoSignOfAttack(payload = Password.class) String password,

		@Nullable @Size(max = 200) @NoSignOfAttack(payload = Password.class) String oldPassword,

		@Nullable UserRoleDTO role,

		@Nullable Boolean locked) {}
