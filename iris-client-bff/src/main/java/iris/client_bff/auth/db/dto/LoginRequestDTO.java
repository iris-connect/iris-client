package iris.client_bff.auth.db.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequestDTO {

	@NotNull
	private String userName;

	@NotNull
	private String password;

}
