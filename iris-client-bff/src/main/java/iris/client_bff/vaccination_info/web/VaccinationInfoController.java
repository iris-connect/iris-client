package iris.client_bff.vaccination_info.web;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfo.Employee;
import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;
import iris.client_bff.vaccination_info.VaccinationInfoService;
import iris.client_bff.vaccination_info.VaccinationStatus;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vaccination-reports")
@Validated
public class VaccinationInfoController {

	private final VaccinationInfoService service;
	private final VaccinationInfoMapper mapper;

	@GetMapping()
	public Page<VaccinationInfoDto.VaccinationReport> getVaccinationInfos(
			@RequestParam Optional<@NoSignOfAttack String> search,
			Pageable pageable) {

		var newPageable = adaptPageable(pageable);

		var vaccInfos = search
				.map(it -> service.search(it, pageable))
				.orElseGet(() -> service.getAll(newPageable));

		return vaccInfos.map(mapper::toVaccinationReportDto);
	}

	private PageRequest adaptPageable(Pageable pageable) {

		var sort = pageable.getSort();
		var orders = sort.map(it -> {
			if ("reportedAt".equals(it.getProperty())) {
				return it.withProperty("metadata.created");
			}

			return it;
		}).toList();

		sort = Sort.by(orders);

		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}

	@GetMapping("/{id}")
	public VaccinationInfoDto.VaccinationReportDetails getDetails(@PathVariable VaccinationInfoIdentifier id) {

		return service.find(id)
				.map(mapper::toVaccinationReportDetailsDto)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid identifier"));
	}

	@Mapper(config = MapStructCentralConfig.class)
	interface VaccinationInfoMapper {

		@Mapping(target = "reportedAt", source = "lastModifiedAt")
		VaccinationInfoDto.VaccinationReport toVaccinationReportDto(VaccinationInfo vaccInfo);

		@Mapping(target = "reportedAt", source = "lastModifiedAt")
		VaccinationInfoDto.VaccinationReportDetails toVaccinationReportDetailsDto(VaccinationInfo vaccInfo);

		@Mapping(target = "eMail", source = "EMail")
		VaccinationInfoDto.ContactPerson toContactPersonDto(VaccinationInfo.ContactPerson contactPerson);

		@Mapping(target = "eMail", source = "email")
		@Mapping(target = "messageDataSelectId", source = "id")
		VaccinationInfoDto.Employee toEmployeeDto(Employee employee);

		default Map<VaccinationStatus, Long> vaccinationStatusCount(VaccinationInfo.VaccinationStatusCount statusCount) {

			return Map.of(VaccinationStatus.VACCINATED, statusCount.getVaccinated(),
					VaccinationStatus.NOT_VACCINATED, statusCount.getNotVaccinated(),
					VaccinationStatus.SUSPICIOUS_PROOF, statusCount.getSuspiciousProof(),
					VaccinationStatus.UNKNOWN, statusCount.getUnknown());
		}
	}
}
