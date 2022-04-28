package iris.client_bff.vaccination_info.message;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.core.Sex;
import iris.client_bff.core.web.dto.AddressWithDefuseData;
import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import iris.client_bff.iris_messages.utils.IrisMessageDataUtils;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfoRepository;
import iris.client_bff.vaccination_info.VaccinationStatus;
import iris.client_bff.vaccination_info.VaccinationType;
import iris.client_bff.vaccination_info.message.dto.ExportSelectionDto;
import iris.client_bff.vaccination_info.message.dto.ImportSelectionDto;
import iris.client_bff.vaccination_info.message.dto.ImportSelectionViewPayloadDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
@Getter
@AllArgsConstructor
class VaccinationMessageDataProcessor implements IrisMessageDataProcessor {

	private final String discriminator = "vaccination-report";

	private final VaccinationInfoRepository vaccinationInfoRepository;
	private final IrisMessageDataUtils messageDataUtils;
	private final MessageSourceAccessor messages;
	private final VaccinationMessageDataMapper vaccinationMessageDataMapper;
	private final ComparableMapper comparableMapper;

	@Override
	public void validateExportSelection(String exportSelection) throws IrisMessageDataException {
		ExportSelectionDto payload = messageDataUtils.parseJSON(exportSelection, ExportSelectionDto.class);
		messageDataUtils.validatePayload(payload);
	}

	@Override
	public void validateImportSelection(String importSelection) throws IrisMessageDataException {
		ImportSelectionDto payload = messageDataUtils.parseJSON(importSelection, ImportSelectionDto.class);
		messageDataUtils.validatePayload(payload);
	}

	@Override
	public String buildPayload(String exportSelection) throws IrisMessageDataException {
		ExportSelectionDto exportSelectionDto = messageDataUtils.parseJSON(exportSelection, ExportSelectionDto.class);
		VaccinationInfo vaccinationInfo = getVaccinationInfo(
				VaccinationInfo.VaccinationInfoIdentifier.of(exportSelectionDto.getReport()));
		Set<String> selectedEmployees = exportSelectionDto.getEmployees();
		Set<VaccinationInfo.Employee> employees = vaccinationInfo.getEmployees().stream()
				.filter((employee -> selectedEmployees.isEmpty() || selectedEmployees.contains(employee.getId().toString())))
				.collect(Collectors.toSet());
		VaccinationMessageDataPayload payload = new VaccinationMessageDataPayload()
				.setFacility(vaccinationMessageDataMapper.toFacilityPayload(vaccinationInfo.getFacility()))
				.setEmployees(vaccinationMessageDataMapper.toEmployeesPayload(employees));
		return messageDataUtils.stringifyJSON(payload);
	}

	@Override
	public void importPayload(String payload) throws IrisMessageDataException {
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);
		VaccinationInfo vaccinationInfo = VaccinationInfo.of(
				messages.getMessage("iris_message.message_data_substitution"),
				vaccinationMessageDataMapper.fromFacilityPayload(messagePayload.getFacility()),
				vaccinationMessageDataMapper.fromEmployeesPayload(messagePayload.getEmployees()));
		vaccinationInfoRepository.save(vaccinationInfo);
	}

	@Override
	public void importPayload(String payload, UUID importTargetId, String importSelection)
			throws IrisMessageDataException {
		VaccinationInfo vaccinationInfo = getVaccinationInfo(VaccinationInfo.VaccinationInfoIdentifier.of(importTargetId));
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);
		ImportSelectionDto selection = messageDataUtils.parseJSON(importSelection, ImportSelectionDto.class);
		messagePayload.getEmployees().stream()
				.filter(employee -> selection.getEmployees().contains(employee.messageDataSelectId()))
				.map(vaccinationMessageDataMapper::fromEmployeePayload)
				.forEach(vaccinationInfo.getEmployees()::add);
		vaccinationInfoRepository.save(vaccinationInfo);
	}

	@Override
	public Object getViewPayload(String payload) throws IrisMessageDataException {
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);
		return vaccinationMessageDataMapper.toVaccinationReportDetailsDto(messagePayload);
	}

	@Override
	public Object getImportSelectionViewPayload(String payload, UUID importTargetId) throws IrisMessageDataException {
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);
		var selectableEmployees = vaccinationMessageDataMapper.toEmployeesDto(messagePayload.getEmployees());
		var duplicateEmployees = getDuplicateEmployees(messagePayload.getEmployees(), importTargetId);
		return ImportSelectionViewPayloadDto.builder()
				.selectables(ImportSelectionViewPayloadDto.Selectables.builder().employees(selectableEmployees).build())
				.duplicates(ImportSelectionViewPayloadDto.Duplicates.builder().employees(duplicateEmployees).build())
				.build();
	}

	private Set<String> getDuplicateEmployees(Set<VaccinationMessageDataPayload.Employee> employees,
			UUID importTargetId) {
		VaccinationInfo vaccinationInfo = getVaccinationInfo(VaccinationInfo.VaccinationInfoIdentifier.of(importTargetId));
		var targetEmployees = vaccinationInfo.getEmployees();
		if (employees == null || targetEmployees == null) {
			return null;
		}
		var comparableTargetEmployees = comparableMapper.fromEmployees(targetEmployees);
		return employees.stream()
				.filter(employee -> {
					var mapped = comparableMapper.fromPayloadEmployee(employee);
					return comparableTargetEmployees.contains(mapped);
				})
				.map(VaccinationMessageDataPayload.Employee::messageDataSelectId)
				.collect(Collectors.toSet());
	}

	private VaccinationInfo getVaccinationInfo(VaccinationInfo.VaccinationInfoIdentifier id) {
		return this.vaccinationInfoRepository.findById(id)
				.orElseThrow(
						() -> new IrisMessageDataException(messages.getMessage("iris_message.invalid_message_data_import_target")));
	}

	private VaccinationMessageDataPayload parsePayload(String payload) throws IrisMessageDataException {
		return messageDataUtils.parseJSON(payload, VaccinationMessageDataPayload.class);
	}

	@Mapper(config = MapStructCentralConfig.class)
	interface ComparableMapper {
		Set<Employee> fromEmployees(Set<VaccinationInfo.Employee> employees);

		@Mapping(target = "eMail", source = "email")
		Employee fromEmployee(VaccinationInfo.Employee employee);

		Employee fromPayloadEmployee(VaccinationMessageDataPayload.Employee employee);

		record Employee(
				String firstName,
				String lastName,
				LocalDate dateOfBirth,
				Sex sex,
				AddressWithDefuseData address,
				String phone,
				String eMail,
				VaccinationType vaccination,
				VaccinationStatus vaccinationStatus) {}
	}
}
