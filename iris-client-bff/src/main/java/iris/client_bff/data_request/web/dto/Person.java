package iris.client_bff.data_request.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic data type of a person.
 */
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Basic data type of a person.")
public class Person {

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("dateOfBirth") @org.springframework.format.annotation.DateTimeFormat(
			iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
	private LocalDate dateOfBirth;

	@JsonProperty("sex")
	private Sex sex = Sex.UNKNOWN;

	@JsonProperty("email")
	private String email;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("mobilePhone")
	private String mobilePhone;

	@JsonProperty("address")
	private Address address;

}
