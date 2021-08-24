package iris.client_bff.core.web.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Address {

  private @NotBlank String street;
  private @NotBlank String houseNumber;
  private @NotBlank String zipCode;
  private @NotBlank String city;
}
