package iris.client_bff.vaccination_info.message;

import java.util.Set;
import java.util.stream.Collectors;

import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfoDataInitializer;
import iris.client_bff.vaccination_info.message.dto.ExportSelectionDto;
import iris.client_bff.vaccination_info.message.dto.ImportSelectionDto;
import iris.client_bff.vaccination_info.message.dto.ImportSelectionViewPayloadDto;
import iris.client_bff.vaccination_info.web.VaccinationInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.web.IrisMessageInsertDto;

@Component
@Slf4j
public class VaccinationMessageTestData {

	public static final VaccinationMessageDataMapper vaccinationMessageDataMapper = Mappers.getMapper(VaccinationMessageDataMapper.class);

	public final VaccinationInfo MOCK_VACCINATION_INFO = VaccinationInfoDataInitializer.createVaccinationInfo();

	public final VaccinationMessageDataPayload MOCK_MESSAGE_DATA_PAYLOAD = getMessageDataPayload(MOCK_VACCINATION_INFO);
	public final String MOCK_MESSAGE_DATA_PAYLOAD_STRING = stringifyJSON(MOCK_MESSAGE_DATA_PAYLOAD);

	public final IrisMessageInsertDto.DataAttachment MOCK_MESSAGE_EXPORT_DATA_ATTACHMENT = getExportDataAttachment();

	public final ExportSelectionDto MOCK_MESSAGE_EXPORT_SELECTION = getExportSelection();
	public final String MOCK_MESSAGE_EXPORT_SELECTION_STRING = stringifyJSON(MOCK_MESSAGE_EXPORT_SELECTION);

	public final ImportSelectionDto MOCK_MESSAGE_IMPORT_SELECTION = getImportSelection();
	public final String MOCK_MESSAGE_IMPORT_SELECTION_STRING = stringifyJSON(MOCK_MESSAGE_IMPORT_SELECTION);

	public final ImportSelectionViewPayloadDto MOCK_MESSAGE_IMPORT_SELECTION_VIEW_PAYLOAD = getImportSelectionDataViewPayload();

	public final VaccinationInfoDto.VaccinationReportDetails MOCK_MESSAGE_DATA_VIEW_PAYLOAD = getDataViewPayload();

	public IrisMessageData getMessageData(IrisMessage message) {
		return new IrisMessageData()
				.setImported(false)
				.setMessage(message)
				.setDiscriminator("vaccination-report")
				.setDescription("test description")
				.setPayload(stringifyJSON(this.getMessageDataPayload(MOCK_VACCINATION_INFO)));
	}

	private VaccinationMessageDataPayload getMessageDataPayload(VaccinationInfo vaccinationInfo) {
		return new VaccinationMessageDataPayload()
				.setFacility(vaccinationMessageDataMapper.toFacilityPayload(vaccinationInfo.getFacility()))
				.setEmployees(vaccinationMessageDataMapper.toEmployeesPayload(vaccinationInfo.getEmployees()));
	}

	private IrisMessageInsertDto.DataAttachment getExportDataAttachment() {
		return new IrisMessageInsertDto.DataAttachment()
				.setDiscriminator("vaccination-report")
				.setDescription("test description")
				.setPayload(getPayloadPojo(this.getExportSelection()).toString());
	}

	private ExportSelectionDto getExportSelection() {
		Set<String> employees = MOCK_VACCINATION_INFO.getEmployees().stream()
				.map((employee -> employee.getId().toString()))
				.collect(Collectors.toSet());
		return new ExportSelectionDto()
				.setReport(MOCK_VACCINATION_INFO.getId().toUuid())
				.setEmployees(employees);
	}

	private ImportSelectionViewPayloadDto getImportSelectionDataViewPayload() {
		var employees = MOCK_MESSAGE_DATA_PAYLOAD.getEmployees();
		var employeesDto = vaccinationMessageDataMapper.toEmployeesDto(employees);
		var employeeIds = employees.stream().map(VaccinationMessageDataPayload.Employee::messageDataSelectId).collect(Collectors.toSet());
		return ImportSelectionViewPayloadDto.builder()
				.selectables(ImportSelectionViewPayloadDto.Selectables.builder().employees(employeesDto).build())
				.duplicates(ImportSelectionViewPayloadDto.Duplicates.builder().employees(employeeIds).build())
				.build();
	}

	private VaccinationInfoDto.VaccinationReportDetails getDataViewPayload() {
		return vaccinationMessageDataMapper.toVaccinationReportDetailsDto(MOCK_MESSAGE_DATA_PAYLOAD);
	}

	private ImportSelectionDto getImportSelection() {
		Set<String> employees = MOCK_VACCINATION_INFO.getEmployees().stream()
				.map(employee -> employee.getId().toString())
				.collect(Collectors.toSet());
		return new ImportSelectionDto().setEmployees(employees);
	}

	private <T> String stringifyJSON(T value) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			return objectMapper.writeValueAsString(value);
		} catch (Exception e) {
			return "";
		}
	}

	private Object getPayloadPojo(Object payload) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			String json = stringifyJSON(payload);
			return objectMapper.readValue(json, Object.class);
		} catch (JsonProcessingException e) {
			// ignored
		}
		return "";
	}
}
