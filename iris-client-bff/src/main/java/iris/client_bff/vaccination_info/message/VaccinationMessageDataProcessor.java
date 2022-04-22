package iris.client_bff.vaccination_info.message;

import iris.client_bff.iris_messages.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import iris.client_bff.iris_messages.utils.IrisMessageDataUtils;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfoRepository;
import iris.client_bff.vaccination_info.message.dto.ExportSelectionDto;
import iris.client_bff.vaccination_info.message.dto.ImportSelectionDto;
import iris.client_bff.vaccination_info.message.dto.ImportSelectionViewPayloadDto;
import iris.client_bff.vaccination_info.web.VaccinationInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Getter
@AllArgsConstructor
public class VaccinationMessageDataProcessor implements IrisMessageDataProcessor {

	private final String discriminator = "vaccination-report";

	private final VaccinationMessageDataBuilder dataBuilder;

	private final IrisMessageDataUtils messageDataUtils;
	private final MessageSourceAccessor messages;
	private final VaccinationMessageDataMapper vaccinationMessageDataMapper;
	private final VaccinationInfoRepository vaccinationInfoRepository;

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
		VaccinationMessageDataPayload payload = this.dataBuilder.buildPayload(exportSelectionDto);
		return messageDataUtils.stringifyJSON(payload);
	}

	@Override
	public void importPayload(String payload) throws IrisMessageDataException {
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);
		VaccinationInfo vaccinationInfo = VaccinationInfo.of(
				messages.getMessage("iris_message.message_data_substitution"),
				vaccinationMessageDataMapper.toFacility(messagePayload.getFacility()),
				vaccinationMessageDataMapper.toEmployees(messagePayload.getEmployees())
		);
		vaccinationInfoRepository.save(vaccinationInfo);
	}

	@Override
	public void importPayload(String payload, UUID importTargetId, String importSelection) throws IrisMessageDataException {
		Optional<VaccinationInfo> optionalVaccinationInfo = vaccinationInfoRepository.findById(VaccinationInfo.VaccinationInfoIdentifier.of(importTargetId));
		if (optionalVaccinationInfo.isEmpty()) {
			throw new IrisMessageDataException(messages.getMessage("iris_message.invalid_message_data_import_target"));
		}
		VaccinationInfo vaccinationInfo = optionalVaccinationInfo.get();
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);
		ImportSelectionDto selection = messageDataUtils.parseJSON(importSelection, ImportSelectionDto.class);
		messagePayload.getEmployees().stream()
				.filter(employee -> selection.getEmployees().contains(employee.messageDataSelectId()))
				.map(vaccinationMessageDataMapper::toEmployee)
				.forEach(employee -> vaccinationInfo.getEmployees().add(employee));
		vaccinationInfoRepository.save(vaccinationInfo);
	}

	@Override
	public Object getViewPayload(String payload) throws IrisMessageDataException {
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);
		return vaccinationMessageDataMapper.toWebDetails(messagePayload);
	}

	@Override
	public Object getImportSelectionViewPayload(String payload, UUID importTargetId) throws IrisMessageDataException {
		VaccinationMessageDataPayload messagePayload = this.parsePayload(payload);

		List<VaccinationInfoDto.Employee> employees = messagePayload.getEmployees().stream()
				.map(vaccinationMessageDataMapper::toWebEmployee)
				.toList();

		//@todo: implement duplicates list
		return ImportSelectionViewPayloadDto.builder()
				.selectables(ImportSelectionViewPayloadDto.Selectables.builder().employees(employees).build())
				.build();
	}

	private VaccinationMessageDataPayload parsePayload(String payload) throws IrisMessageDataException {
		return messageDataUtils.parseJSON(payload, VaccinationMessageDataPayload.class);
	}

}
