package iris.client_bff.vaccination_info;

import lombok.Value;

import java.time.Duration;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("iris.client.vaccinfo")
@ConstructorBinding
@Validated
@Value
public class VaccinationInfoProperties {

	/**
	 * Defines the {@link Duration} after that a vaccination info announcement will be expire.
	 */
	@NotNull
	private final Duration expirationDuration;
}
