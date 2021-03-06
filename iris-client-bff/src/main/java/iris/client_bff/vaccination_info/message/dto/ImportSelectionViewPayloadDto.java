package iris.client_bff.vaccination_info.message.dto;

import iris.client_bff.vaccination_info.web.VaccinationInfoDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ImportSelectionViewPayloadDto {
	private Selectables selectables;
	private Duplicates duplicates;

	@Data
	@Builder
	public static class Selectables {
		Set<VaccinationInfoDto.Employee> employees;
	}

	@Data
	@Builder
	public static class Duplicates {
		Set<String> employees;
	}
}
