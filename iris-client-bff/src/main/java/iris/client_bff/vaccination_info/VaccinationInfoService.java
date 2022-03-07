package iris.client_bff.vaccination_info;

import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VaccinationInfoService {

	private final ProxyServiceClient proxyClient;
	private final VaccinationInfoAnnouncementRepository vacInfos;
	private final VaccinationInfoProperties properties;

	public VaccinationInfoAnnouncement announceVaccinationInfo(String externalId) {

		log.trace("Create VaccinationInfo and announcement");

		String announcementToken;
		try {
			announcementToken = proxyClient.announce(Instant.now().plus(properties.getExpirationDuration()));
		} catch (IRISAnnouncementException e) {

			var msg = "Proxy announcement failed";
			log.error(msg + ": ", e);

			throw new VaccinationInfoAnnouncementException(msg, e);
		}

		var announcement = VaccinationInfoAnnouncement.of(externalId, announcementToken);

		log.debug("Created VaccinationInfo and announcement successful");

		return vacInfos.save(announcement);
	}

	public void deleteAnnouncement(AnnouncementIdentifier id) {
		vacInfos.deleteById(id);
	}
}
