package iris.client_bff.users.web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserListDTO(@JsonProperty("users") List<UserDTO> userDTOs) {}
