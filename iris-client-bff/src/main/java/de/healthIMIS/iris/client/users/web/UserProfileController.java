package de.healthIMIS.iris.client.users.web;

import de.healthIMIS.iris.client.users.UserDetailsServiceImpl;
import de.healthIMIS.iris.client.users.entities.UserAccount;
import de.healthIMIS.iris.client.users.web.dto.UserDTO;
import java.security.Principal;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user-profile")
public class UserProfileController {

  private final UserDetailsServiceImpl userService;

  @GetMapping
  public UserDTO getUserProfile(Principal principal) {
    Optional<UserAccount> byUsername = userService.findByUsername(principal.getName());
    if (byUsername.isEmpty()) {
      return null;
    } else {
      return UserMappers.map(byUsername.get());
    }
  }
}
