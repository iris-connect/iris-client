package iris.client_bff.vaccination_info.eps;

import iris.client_bff.core.Sex;
import iris.client_bff.core.validation.Base64;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.core.validation.NoSignOfAttack.Phone;
import iris.client_bff.core.validation.PhoneNumber;
import iris.client_bff.core.web.dto.Address;
import iris.client_bff.vaccination_info.VaccinationStatus;
import iris.client_bff.vaccination_info.VaccinationType;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcParam;

/**
 * @author Jens Kutzsche
 */
@Validated
public interface VaccinationInfoController {

	@JsonRpcErrors({
			@JsonRpcError(exception = ConstraintViolationException.class, code = -32600),
			@JsonRpcError(exception = InvalidPublicKeyException.class, code = -32600)
	})
	AnnouncementResultDto announceVaccinationInfoList(
			@JsonRpcParam(value = "announcementData") @Valid AnnouncementDataDto announcementData);

	@JsonRpcErrors({
			@JsonRpcError(exception = ConstraintViolationException.class, code = -32600),
			@JsonRpcError(exception = IllegalArgumentException.class, code = -32600)
	})
	String submitVaccinationInfoList(
			@JsonRpcParam(value = "dataAuthorizationToken") @NotNull UUID dataAuthorizationToken,
			@JsonRpcParam(value = "facility") @Valid Facility facility,
			@JsonRpcParam(value = "employees") @NotEmpty Set<@Valid Employee> employees);

	// DTOs Announcement
	static record AnnouncementDataDto(

			@NotBlank @NoSignOfAttack String externalId,
			@NotBlank @Base64 @NoSignOfAttack String submitterPublicKey) {}

	static record AnnouncementResultDto(

			@NotBlank String hdPublicKey,
			@NotBlank String iv,
			@NotBlank String tokens) {}

	static record Tokens(

			@NotBlank String cat,
			@NotBlank String dat) {}

	// DTOs Submission
	static record Facility(

			@NotBlank @NoSignOfAttack String name,
			@NotNull @Valid Address address,
			@NotNull @Valid ContactPerson contactPerson) {}

	static record ContactPerson(

			@NotBlank @NoSignOfAttack String firstName,
			@NotBlank @NoSignOfAttack String lastName,
			@NotBlank @Email @NoSignOfAttack String eMail,
			@NotBlank @PhoneNumber @NoSignOfAttack(payload = Phone.class) String phone) {}

	static record Employee(

			@NotBlank @NoSignOfAttack String firstName,
			@NotBlank @NoSignOfAttack String lastName,
			@NotNull @Past LocalDate dateOfBirth,
			@NotNull Sex sex,
			@NotNull @Valid Address address,
			@Nullable @PhoneNumber @NoSignOfAttack(payload = Phone.class) String phone,
			@Nullable @Email @NoSignOfAttack String eMail,
			@NotNull VaccinationType vaccination,
			@NotNull VaccinationStatus vaccinationStatus) {}
}
