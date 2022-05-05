package iris.client_bff.users.web.dto;

import iris.client_bff.core.validation.AttackDetector.Password;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.users.web.ValidPassword;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserInsertDTO(

		@Nullable @Size(max = 200) @NoSignOfAttack String firstName,

		@Nullable @Size(max = 200) @NoSignOfAttack String lastName,

		@NotBlank @Size(max = 50) @NoSignOfAttack String userName,

		@ValidPassword @NotBlank @Size(max = 200) @NoSignOfAttack(payload = Password.class) String password,

		@NotNull UserRoleDTO role,

		boolean locked) {}
