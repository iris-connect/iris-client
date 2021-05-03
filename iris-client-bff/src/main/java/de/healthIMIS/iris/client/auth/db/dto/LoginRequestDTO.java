package de.healthIMIS.iris.client.auth.db.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequestDTO {

  @NotNull
  private String userName;

  @NotNull
  private String password;

}
