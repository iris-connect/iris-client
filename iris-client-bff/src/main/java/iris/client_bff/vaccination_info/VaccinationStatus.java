package iris.client_bff.vaccination_info;

import static iris.client_bff.core.serialization.FlexibleEnum.*;

import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VaccinationStatus {
	VACCINATED, NOT_VACCINATED, SUSPICIOUS_PROOF, UNKNOWN;

	@JsonCreator
	public static VaccinationStatus of(@NonNull String string) {
		return VaccinationStatus.valueOf(cleanStringForEnum(string));
	}
}
