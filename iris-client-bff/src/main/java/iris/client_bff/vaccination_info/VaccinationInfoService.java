package iris.client_bff.vaccination_info;

import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.database.HibernateSearcher;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import iris.client_bff.vaccination_info.VaccinationInfo.Employee;
import iris.client_bff.vaccination_info.VaccinationInfo.Facility;
import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;
import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VaccinationInfoService {

	private static final String[] FIELDS = { "facility.name_search", "facility.address.street",
			"facility.address.zipCode", "facility.address.city" };

	private final ProxyServiceClient proxyClient;
	private final VaccinationInfoAnnouncementRepository announcements;
	private final VaccinationInfoRepository vaccInfos;
	private final VaccinationInfoService.Properties properties;
	private final AlertService alertService;
	private final HibernateSearcher searcher;

	public VaccinationInfoAnnouncement announceVaccinationInfo(String externalId) {

		log.trace("Create VaccinationInfoAnnouncement");

		String announcementToken;
		try {
			announcementToken = proxyClient.announce(Instant.now().plus(properties.getExpirationDuration()));
		} catch (IRISAnnouncementException e) {

			var msg = "Proxy announcement failed";
			log.error(msg + ": ", e);

			throw new VaccinationInfoAnnouncementException(msg, e);
		}

		var announcement = VaccinationInfoAnnouncement.of(externalId, announcementToken);

		announcement = announcements.save(announcement);

		log.debug("Created VaccinationInfoAnnouncement successful");

		return announcement;
	}

	public void deleteAnnouncement(AnnouncementIdentifier id) {
		announcements.deleteById(id);
	}

	public void createVaccinationInfo(AnnouncementIdentifier dataAuthorizationToken, Facility facility,
			Set<Employee> employees) {

		log.trace("Create VaccinationInfo");

		var announcementOpt = announcements.findById(dataAuthorizationToken);

		var announcement = announcementOpt.orElseThrow(() -> {

			alertService.createAlertMessage("Submission for unknown Token - possible attack",
					String.format("Vaccination info submission for unknown DAT occurred: (Data Authorization Token = %s)",
							LogHelper.obfuscateAtStart8(dataAuthorizationToken.toString())));

			throw new IllegalArgumentException("Unknown dataAuthorizationToken: " + dataAuthorizationToken);
		});

		var vaccInfo = VaccinationInfo.of(announcement.getExternalId(), facility, employees);

		vaccInfos.save(vaccInfo);

		log.debug("Created VaccinationInfo successful");
	}

	public Page<VaccinationInfo> getAll(Pageable pageable) {
		return vaccInfos.findAll(pageable);
	}

	public Page<VaccinationInfo> search(String searchString, Pageable pageable) {

		var result = searcher.search(
				searchString,
				pageable,
				FIELDS,
				it -> it,
				VaccinationInfo.class);

		return new PageImpl<>(result.hits(), pageable, result.total().hitCount());
	}

	public Optional<VaccinationInfo> find(VaccinationInfoIdentifier id) {
		return vaccInfos.findById(id);
	}

	@ConfigurationProperties("iris.client.vaccinfo")
	@ConstructorBinding
	@Validated
	@Value
	static class Properties {

		/**
		 * Defines the {@link Duration} after that a vaccination info announcement will be expire.
		 */
		@NotNull
		private Duration expirationDuration;
	}
}
