package iris.client_bff.users.web;

import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.web.dto.UserDTO;
import iris.client_bff.users.web.dto.UserRoleDTO;

public class UserMappers {

	public static UserDTO map(UserAccount account, UserDetailsServiceImpl userService) {

		return UserDTO.builder()
				.userName(account.getUserName())
				.firstName(account.getFirstName())
				.lastName(account.getLastName())
				.id(account.getId().toString())
				.role(UserRoleDTO.valueOf(account.getRole().name()))
				.createdAt(account.getCreatedAt())
				.lastModifiedAt(account.getLastModifiedAt())
				.createdBy(userService.findByUuid(account.getCreatedBy()).map(UserMappers::getFullName).orElse(null))
				.lastModifiedBy(userService.findByUuid(account.getLastModifiedBy()).map(UserMappers::getFullName).orElse(null))
				.build();
	}

	private static String getFullName(UserAccount user) {
		return user.getFirstName() + " " + user.getLastName();
	}
}
