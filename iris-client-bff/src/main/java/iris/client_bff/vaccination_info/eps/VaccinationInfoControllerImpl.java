package iris.client_bff.vaccination_info.eps;

import iris.client_bff.core.model.Address;
import iris.client_bff.vaccination_info.EncryptionService;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncementException;
import iris.client_bff.vaccination_info.VaccinationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
@Slf4j
class VaccinationInfoControllerImpl implements VaccinationInfoController {

	private final VaccinationInfoService service;
	private final EncryptionService encryptionService;
	private final ObjectMapper mapper;

	@Override
	public AnnouncementResultDto announceVaccinationInfoList(AnnouncementDataDto announcementData) {

		log.debug("Start announce vaccination info list (JSON-RPC interface)");

		var vacInfo = service.announceVaccinationInfo(announcementData.externalId());

		log.trace("Finish announce vaccination info list (JSON-RPC interface)");

		return encryptAndCreateResult(vacInfo, announcementData.submitterPublicKey());
	}

	@Override
	public String submitVaccinationInfoList(UUID dataAuthorizationToken, Facility facility, Set<Employee> employees) {

		log.debug("Start submit vaccination info list (JSON-RPC interface)");

		service.createVaccinationInfo(AnnouncementIdentifier.of(dataAuthorizationToken), map(facility), map(employees));

		log.trace("Finish submit vaccination info list (JSON-RPC interface)");

		return "OK";
	}

	private AnnouncementResultDto encryptAndCreateResult(VaccinationInfoAnnouncement announcement,
			String submitterPublicKeyBase64) {

		var tokens = new Tokens(
				announcement.getAnnouncementToken(),
				announcement.getId().toString());

		PublicKey submitterPublicKey;
		try {
			submitterPublicKey = encryptionService.decodeFromBase64(submitterPublicKeyBase64);
		} catch (GeneralSecurityException e) {

			var msg = "The passed public key contains errors and cannot be used";
			log.error(msg + ": ", e);

			service.deleteAnnouncement(announcement.getId());

			throw new InvalidPublicKeyException("submitterPublicKey: " + msg, e);
		}

		try {
			var keyPair = encryptionService.generateKeyPair();
			var key = encryptionService.generateAgreedKey(keyPair.getPrivate(), submitterPublicKey);

			var pubKeyBase64 = encryptionService.encodeToBase64(keyPair.getPublic());
			var encryptionData = encryptionService.encryptAndEncode(key, mapper.writeValueAsString(tokens));

			return new AnnouncementResultDto(pubKeyBase64, encryptionData.iv(), encryptionData.data());
		} catch (JsonProcessingException | GeneralSecurityException e) {

			var msg = e instanceof JsonProcessingException
					? "Can't write tokens to JSON"
					: "Error during token encryption (response to the announcement)";
			log.error(msg + ": ", e);

			service.deleteAnnouncement(announcement.getId());

			throw new VaccinationInfoAnnouncementException(msg, e);
		}
	}

	private VaccinationInfo.Facility map(Facility facilityDto) {

		var address = map(facilityDto.address());

		var personDto = facilityDto.contactPerson();
		var contactPerson = VaccinationInfo.ContactPerson.of(personDto.firstName(), personDto.lastName(), personDto.eMail(),
				personDto.phone());

		return VaccinationInfo.Facility.of(facilityDto.name(), address, contactPerson);
	}

	private Set<VaccinationInfo.Employee> map(Set<Employee> employees) {

		return employees.stream()
				.map(this::map)
				.collect(Collectors.toSet());
	}

	private VaccinationInfo.Employee map(Employee employee) {

		return VaccinationInfo.Employee.of(
				employee.firstName(),
				employee.lastName(),
				employee.dateOfBirth(),
				employee.sex(),
				map(employee.address()),
				employee.eMail(),
				employee.phone(),
				employee.vaccination(),
				employee.vaccinationStatus());
	}

	private Address map(iris.client_bff.core.web.dto.Address addressDto) {

		return Address.builder()
				.street(addressDto.getStreet())
				.houseNumber(addressDto.getHouseNumber())
				.city(addressDto.getCity())
				.zipCode(addressDto.getZipCode())
				.build();
	}
}
