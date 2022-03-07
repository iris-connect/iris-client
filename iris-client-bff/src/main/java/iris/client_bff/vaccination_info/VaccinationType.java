package iris.client_bff.vaccination_info;

import static iris.client_bff.core.model.FlexibleEnum.*;

import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Jens Kutzsche
 */
public enum VaccinationType {
	COVID_19;

	@JsonCreator
	public static VaccinationType of(@NonNull String string) {
		return VaccinationType.valueOf(cleanStringForEnum(string));
	}
}
