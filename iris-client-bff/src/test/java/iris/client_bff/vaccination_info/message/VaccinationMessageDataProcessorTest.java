package iris.client_bff.vaccination_info.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import javax.validation.Validator;

import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfoRepository;
import iris.client_bff.vaccination_info.message.dto.ExportSelectionDto;
import iris.client_bff.vaccination_info.message.dto.ImportSelectionDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import iris.client_bff.config.JacksonConfig;
import iris.client_bff.core.validation.AttackDetector;
import iris.client_bff.iris_messages.utils.IrisMessageDataUtils;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class VaccinationMessageDataProcessorTest {

	VaccinationMessageTestData messageTestData;

	VaccinationMessageDataProcessor messageDataProcessor;

	private final VaccinationMessageDataMapper vaccinationMessageDataMapper = Mappers.getMapper(VaccinationMessageDataMapper.class);

	private final VaccinationMessageDataProcessor.ComparableMapper comparableMapper = Mappers.getMapper(VaccinationMessageDataProcessor.ComparableMapper.class);

	@Mock
	VaccinationInfoRepository vaccinationInfoRepository;

	@Mock
	Validator validator;

	@Mock
	AttackDetector attackDetector;

	@Mock
	MessageSourceAccessor messages;

	IrisMessageDataUtils messageDataUtils;

	ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		this.messageTestData = new VaccinationMessageTestData();
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper
				.setInjectableValues(new InjectableValues.Std().addValue(JacksonConfig.ATTACK_DETECTOR, attackDetector));

		this.messageDataUtils = new IrisMessageDataUtils(this.validator, this.objectMapper);

		this.messageDataProcessor = new VaccinationMessageDataProcessor(
				this.vaccinationInfoRepository,
				this.messageDataUtils,
				this.messages,
				this.vaccinationMessageDataMapper,
				this.comparableMapper
		);
	}

	@Test
	void validateExportSelection() {

		when(this.validator.validate(any(ExportSelectionDto.class))).thenReturn(Set.of());

		this.messageDataProcessor
				.validateExportSelection(this.messageTestData.MOCK_MESSAGE_EXPORT_SELECTION_STRING);

		verify(this.validator).validate(this.messageTestData.MOCK_MESSAGE_EXPORT_SELECTION);
	}

	@Test
	void validateImportSelection() {

		when(this.validator.validate(any(ImportSelectionDto.class))).thenReturn(Set.of());

		this.messageDataProcessor
				.validateImportSelection(this.messageTestData.MOCK_MESSAGE_IMPORT_SELECTION_STRING);

		verify(this.validator).validate(this.messageTestData.MOCK_MESSAGE_IMPORT_SELECTION);
	}

	@Test
	void buildPayload() {

		var id = this.messageTestData.MOCK_MESSAGE_EXPORT_SELECTION.getReport();

		when(this.vaccinationInfoRepository.findById(any(VaccinationInfo.VaccinationInfoIdentifier.class)))
				.thenReturn(Optional.of(this.messageTestData.MOCK_VACCINATION_INFO));

		var result = this.messageDataProcessor
				.buildPayload(this.messageTestData.MOCK_MESSAGE_EXPORT_SELECTION_STRING);

		verify(this.vaccinationInfoRepository).findById(VaccinationInfo.VaccinationInfoIdentifier.of(id));

		// parsing the result to have a comparable object
		var payload = messageDataUtils.parseJSON(result, VaccinationMessageDataPayload.class);

		assertEquals(this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD.getFacility(), payload.getFacility());
		assertEquals(this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD.getEmployees().size(), payload.getEmployees().size());
		// employees should have different messageDataSelectId values
		assertNotEquals(this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD.getEmployees(), payload.getEmployees());
	}

	@Test
	void importPayloadAndAdd() {

		when(this.vaccinationInfoRepository.save(any(VaccinationInfo.class)))
				.thenReturn(this.messageTestData.MOCK_VACCINATION_INFO);

		this.messageDataProcessor.importPayload(this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD_STRING);

		verify(this.attackDetector)
				.isPossibleAttack(this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD.getFacility().name(),
						"java.lang.String#name",
						true);

		verify(this.vaccinationInfoRepository).save(any(VaccinationInfo.class));

	}

	@Test
	void importPayloadAndUpdate() {

		when(this.vaccinationInfoRepository.findById(any(VaccinationInfo.VaccinationInfoIdentifier.class)))
				.thenReturn(Optional.of(this.messageTestData.MOCK_VACCINATION_INFO));

		when(this.vaccinationInfoRepository.save(any(VaccinationInfo.class)))
				.thenReturn(this.messageTestData.MOCK_VACCINATION_INFO);

		this.messageDataProcessor.importPayload(
				this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD_STRING,
				this.messageTestData.MOCK_VACCINATION_INFO.getId().toUuid(),
				this.messageTestData.MOCK_MESSAGE_IMPORT_SELECTION_STRING
		);

		verify(this.attackDetector)
				.isPossibleAttack(this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD.getFacility().name(),
						"java.lang.String#name",
						true);

		verify(this.vaccinationInfoRepository).findById(any(VaccinationInfo.VaccinationInfoIdentifier.class));
		verify(this.vaccinationInfoRepository).save(any(VaccinationInfo.class));
	}

	@Test
	void getViewPayload() {

		var result = this.messageDataProcessor
				.getViewPayload(this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD_STRING);

		assertEquals(result, this.messageTestData.MOCK_MESSAGE_DATA_VIEW_PAYLOAD);
	}

	@Test
	void getImportSelectionViewPayload() {

		when(this.vaccinationInfoRepository.findById(any(VaccinationInfo.VaccinationInfoIdentifier.class)))
				.thenReturn(Optional.of(this.messageTestData.MOCK_VACCINATION_INFO));

		var result = this.messageDataProcessor.getImportSelectionViewPayload(
				this.messageTestData.MOCK_MESSAGE_DATA_PAYLOAD_STRING,
				this.messageTestData.MOCK_VACCINATION_INFO.getId().toUuid());

		verify(this.vaccinationInfoRepository).findById(any(VaccinationInfo.VaccinationInfoIdentifier.class));

		assertEquals(result, this.messageTestData.MOCK_MESSAGE_IMPORT_SELECTION_VIEW_PAYLOAD);

	}
}
