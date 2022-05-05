package iris.client_bff.users.web.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserDTO(
		String id,
		String firstName,
		String lastName,
		String userName,
		UserRoleDTO role,
		boolean locked,

		Instant createdAt,
		Instant lastModifiedAt,
		String createdBy,
		String lastModifiedBy) {}
