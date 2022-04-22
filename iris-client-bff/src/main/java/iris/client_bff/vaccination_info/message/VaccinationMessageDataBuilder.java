package iris.client_bff.vaccination_info.message;

import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfoService;
import iris.client_bff.vaccination_info.message.dto.ExportSelectionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Getter
@AllArgsConstructor
public class VaccinationMessageDataBuilder {

	private final VaccinationInfoService vaccinationInfoService;
	private final VaccinationMessageDataMapper vaccinationMessageDataMapper;

	VaccinationMessageDataPayload buildPayload(ExportSelectionDto exportSelectionDto) throws IrisMessageDataException {
		VaccinationInfo.VaccinationInfoIdentifier id = VaccinationInfo.VaccinationInfoIdentifier.of(exportSelectionDto.getReport());
		Optional<VaccinationInfo> optionalVaccinationInfo = vaccinationInfoService.find(id);
		if (optionalVaccinationInfo.isEmpty()) {
			throw new IrisMessageDataException("iris_message.message_data_mismatch");
		}
		VaccinationInfo vaccinationInfo = optionalVaccinationInfo.get();
		List<String> selectedEmployees = exportSelectionDto.getEmployees();
		Set<VaccinationInfo.Employee> employees = vaccinationInfo.getEmployees().stream()
				.filter((employee -> selectedEmployees.isEmpty() || selectedEmployees.contains(employee.getId().toString())))
				.collect(Collectors.toSet());
		return new VaccinationMessageDataPayload()
				.setFacility(vaccinationMessageDataMapper.toPayloadFacility(vaccinationInfo.getFacility()))
				.setEmployees(vaccinationMessageDataMapper.toPayloadEmployees(employees));
	}
}
