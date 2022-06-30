package iris.client_bff.auth.db.web;

import iris.client_bff.auth.db.MfAuthenticationProperties;
import iris.client_bff.auth.db.MfAuthenticationProperties.MfAuthenticationOptions;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mfa/config")
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
@RequiredArgsConstructor
class MfAuthenticationConfigController {

	private final MfAuthenticationProperties properties;

	@GetMapping
	MfaConfigResponse getMfaConfiguration() {
		return new MfaConfigResponse(properties.getOption());
	}

	static record MfaConfigResponse(MfAuthenticationOptions mfaOption) {}
}
