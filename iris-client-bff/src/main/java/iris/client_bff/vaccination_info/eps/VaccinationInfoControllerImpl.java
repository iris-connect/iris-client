package iris.client_bff.vaccination_info.eps;

import iris.client_bff.config.JsonRpcDataValidator;
import iris.client_bff.vaccination_info.EncryptionService;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncementException;
import iris.client_bff.vaccination_info.VaccinationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

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

	private final JsonRpcDataValidator jsonRpcDataValidator;
	private final VaccinationInfoService service;
	private final EncryptionService encryptionService;
	private final ObjectMapper mapper;

	@Override
	public AnnouncementResultDto announceVaccinationInfoList(AnnouncementDataDto announcementData) {

		log.debug("Start announce vaccination info list (JSON-RPC interface)");

		jsonRpcDataValidator.validateData(announcementData);

		var vacInfo = service.announceVaccinationInfo(announcementData.externalId());

		log.trace("Finish announce vaccination info list (JSON-RPC interface)");

		return encryptAndCreateResult(vacInfo, announcementData.submitterPublicKey());
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
}
