package iris.client_bff.users.web;

import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-profile")
@RequiredArgsConstructor
@Validated
class UserProfileController {

	private final UserMapper userMapper;
	private final UserService userService;

	@GetMapping
	public UserDtos.Output getUserProfile(@AuthenticationPrincipal UserAccount principal) {
		return userMapper.toDto(principal);
	}

	@GetMapping("/mfa")
	public ResponseEntity<MfaResponse> getMfaQrUri(
			@AuthenticationPrincipal @NotNull(message = "There is no current user to interact with!") UserAccount principal) {

		return ResponseEntity.ok(new MfaResponse(userService.generateQrCodeImageUri(principal), principal.getMfaSecret()));
	}

	@PostMapping("/mfa")
	public ResponseEntity<MfaResponse> modifyUseMfa(@RequestParam("useMfa") final boolean useMfa,
			@AuthenticationPrincipal @NotNull(message = "There is no current user to interact with!") UserAccount principal) {

		var updatedUser = userService.updateUseMfa(principal, useMfa);

		return useMfa
				? ResponseEntity
						.ok(new MfaResponse(userService.generateQrCodeImageUri(updatedUser), updatedUser.getMfaSecret()))
				: ResponseEntity.noContent().build();
	}

	@DeleteMapping("/mfa")
	public ResponseEntity<MfaResponse> resetMfaSecret(
			@AuthenticationPrincipal @NotNull(message = "There is no current user to interact with!") UserAccount principal) {

		userService.resetMfaSecret(principal);

		return ResponseEntity.ok(new MfaResponse(userService.generateQrCodeImageUri(principal), principal.getMfaSecret()));
	}

	@PostMapping("/mfa/otp")
	public ResponseEntity<String> verifyOtp(@RequestBody @Valid OtpDto otpDto,
			@AuthenticationPrincipal @NotNull(message = "There is no current user to interact with!") UserAccount principal) {

		var otpValid = userService.finishEnrollment(principal, otpDto.otp());

		return otpValid
				? ResponseEntity.ok("The OTP is valid.")
				: ResponseEntity.badRequest().body("The OTP isn't valid.");
	}

	static record OtpDto(@NotBlank String otp) {}

	static record MfaResponse(String qrCodeImageUri, String mfaSecret) {}
}
