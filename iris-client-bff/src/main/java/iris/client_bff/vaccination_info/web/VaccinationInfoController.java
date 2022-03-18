package iris.client_bff.vaccination_info.web;

import iris.client_bff.core.Sex;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.core.web.dto.Address;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfo.Employee;
import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;
import iris.client_bff.vaccination_info.VaccinationInfoService;
import iris.client_bff.vaccination_info.VaccinationStatus;
import iris.client_bff.vaccination_info.VaccinationType;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

	@GetMapping()
	public Page<VaccinationReportDto> getVaccinationInfos(
			@RequestParam Optional<@NoSignOfAttack String> search,
			Pageable pageable) {

		var newPageable = adaptPageable(pageable);

		var vaccInfos = search
				.map(it -> service.search(it, pageable))
				.orElseGet(() -> service.getAll(newPageable));

		return vaccInfos.map(this::map);
	}

	private PageRequest adaptPageable(Pageable pageable) {

		var sort = pageable.getSort();
		var orders = sort.map(it -> {
			if (it.getProperty().equals("reportedAt")) {
				return it.withProperty("metadata.created");
			}

			return it;
		}).toList();

		sort = Sort.by(orders);

		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}

	@GetMapping("/{id}")
	public VaccinationReportDetailsDto getDetails(@PathVariable VaccinationInfoIdentifier id) {

		return service.find(id)
				.map(this::mapDetails)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid identifier"));
	}

	private VaccinationReportDto map(VaccinationInfo vaccInfo) {

		var statusCountMap = vaccInfo.getEmployees().stream()
				.collect(Collectors.groupingBy(
						VaccinationInfo.Employee::getVaccinationStatus,
						Collectors.counting()));

		return new VaccinationReportDto(
				vaccInfo.getId().toString(),
				map(vaccInfo.getFacility()),
				vaccInfo.getLastModifiedAt(),
				statusCountMap);
	}

	private VaccinationReportDetailsDto mapDetails(VaccinationInfo vaccInfo) {

		var report = map(vaccInfo);

		return new VaccinationReportDetailsDto(
				report.id(),
				report.facility,
				report.reportedAt,
				report.vaccinationStatusCount,
				map(vaccInfo.getEmployees()));
	}

	private FacilityDto map(VaccinationInfo.Facility facility) {

		return new FacilityDto(
				facility.getName(),
				map(facility.getAddress()),
				map(facility.getContactPerson()));
	}

	private Address map(iris.client_bff.core.model.Address address) {

		return Address.builder()
				.street(address.getStreet())
				.houseNumber(address.getHouseNumber())
				.zipCode(address.getZipCode())
				.city(address.getCity())
				.build();
	}

	private ContactPersonDto map(VaccinationInfo.ContactPerson contactPerson) {

		return new ContactPersonDto(
				contactPerson.getFirstName(),
				contactPerson.getLastName(),
				contactPerson.getEMail(),
				contactPerson.getPhone());
	}

	private Set<EmployeeDto> map(Set<Employee> employees) {
		return employees.stream()
				.map(this::map)
				.collect(Collectors.toSet());
	}

	private EmployeeDto map(Employee employee) {

		return new EmployeeDto(
				employee.getFirstName(),
				employee.getLastName(),
				employee.getDateOfBirth(),
				employee.getSex(),
				map(employee.getAddress()),
				employee.getPhone(),
				employee.getEmail(),
				employee.getVaccination(),
				employee.getVaccinationStatus());
	}

	record VaccinationReportDto(
			String id,
			FacilityDto facility,
			Instant reportedAt,
			Map<VaccinationStatus, Long> vaccinationStatusCount) {}

	record VaccinationReportDetailsDto(
			String id,
			FacilityDto facility,
			Instant reportedAt,
			Map<VaccinationStatus, Long> vaccinationStatusCount,
			Set<EmployeeDto> employees) {}

	record FacilityDto(

			String name,
			Address address,
			ContactPersonDto contactPerson) {}

	record ContactPersonDto(

			String firstName,
			String lastName,
			String eMail,
			String phone) {}

	record EmployeeDto(

			String firstName,
			String lastName,
			LocalDate dateOfBirth,
			Sex sex,
			Address address,
			String phone,
			String eMail,
			VaccinationType vaccination,
			VaccinationStatus vaccinationStatus) {}
}
