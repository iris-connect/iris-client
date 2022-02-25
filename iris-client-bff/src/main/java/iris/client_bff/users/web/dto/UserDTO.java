package iris.client_bff.users.web.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class UserDTO {

	private String id;
	private String firstName;
	private String lastName;
	private String userName;
	private UserRoleDTO role;

	private Instant createdAt;
	private Instant lastModifiedAt;
	private String createdBy;
	private String lastModifiedBy;
}
