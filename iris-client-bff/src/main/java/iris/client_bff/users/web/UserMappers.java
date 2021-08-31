package iris.client_bff.users.web;

import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.web.dto.UserDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;

public class UserMappers {

	public static UserDTO map(UserAccount account) {
		return new UserDTO()
				.userName(account.getUserName())
				.firstName(account.getFirstName())
				.lastName(account.getLastName())
				.id(account.getUser_id().toString())
				.role(UserRoleDTO.valueOf(account.getRole().name()));
	}

}
