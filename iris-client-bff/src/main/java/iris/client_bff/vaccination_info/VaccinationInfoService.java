package iris.client_bff.vaccination_info;

import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import iris.client_bff.vaccination_info.VaccinationInfo.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
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
	private final VaccinationInfoRepository vacInfos;

	public VaccinationInfo announceVaccinationInfo(String externalId) {

		log.trace("Create VaccinationInfo and announcement");

		String announcementToken;
		try {
			announcementToken = proxyClient.announce(Instant.now().plus(Duration.ofHours(2)));
		} catch (IRISAnnouncementException e) {

			log.error("Announcement failed: ", e);

			throw new IRISDataRequestException(e);
		}

		var vacInfo = VaccinationInfo.of(externalId, announcementToken, Status.ANNOUNCED);

		log.debug("Created VaccinationInfo and announcement successful");

		return vacInfos.save(vacInfo);
	}
}
