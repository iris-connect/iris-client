package de.healthIMIS.iris.client.users.web.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

/**
 * UserList
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-04-23T14:16:42.381524+02:00[Europe/Berlin]")
public class UserListDTO {
  @JsonProperty("users")
  @Valid
  private List<UserDTO> userDTOS = null;

  public UserListDTO users(List<UserDTO> userDTOS) {
    this.userDTOS = userDTOS;
    return this;
  }

  public UserListDTO addUsersItem(UserDTO usersItem) {
    if (this.userDTOS == null) {
      this.userDTOS = new ArrayList<>();
    }
    this.userDTOS.add(usersItem);
    return this;
  }

  /**
   * Get users
   * @return users
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<UserDTO> getUsers() {
    return userDTOS;
  }

  public void setUsers(List<UserDTO> userDTOS) {
    this.userDTOS = userDTOS;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserListDTO userListDTO = (UserListDTO) o;
    return Objects.equals(this.userDTOS, userListDTO.userDTOS);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userDTOS);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserList {\n");
    
    sb.append("    users: ").append(toIndentedString(userDTOS)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

