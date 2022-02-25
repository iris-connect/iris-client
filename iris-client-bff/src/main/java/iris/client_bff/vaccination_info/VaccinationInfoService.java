package iris.client_bff.vaccination_info;

import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
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

			log.error("Announcement failed: ", e);

			throw new IRISDataRequestException(e);
		}

		var vacInfo = VaccinationInfoAnnouncement.of(externalId, announcementToken);

		log.debug("Created VaccinationInfo and announcement successful");

		return vacInfos.save(vacInfo);
	}
}
