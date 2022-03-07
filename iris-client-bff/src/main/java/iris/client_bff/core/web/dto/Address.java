package iris.client_bff.core.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.validation.NoSignOfAttack;
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

	private @NotBlank @NoSignOfAttack String street;
	private @NotBlank @NoSignOfAttack String houseNumber;
	private @NotBlank @NoSignOfAttack String zipCode;
	private @NotBlank @NoSignOfAttack String city;
}
