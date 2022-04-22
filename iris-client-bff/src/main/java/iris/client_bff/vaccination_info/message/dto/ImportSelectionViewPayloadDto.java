package iris.client_bff.vaccination_info.message.dto;

import java.util.List;

import iris.client_bff.vaccination_info.web.VaccinationInfoDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportSelectionViewPayloadDto {
	private Selectables selectables;
	private Duplicates duplicates;

	@Data
	@Builder
	public static class Selectables {
		List<VaccinationInfoDto.Employee> employees;
	}

	@Data
	@Builder
	public static class Duplicates {
		List<String> employees;
	}
}
