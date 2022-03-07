package iris.client_bff.vaccination_info;

import iris.client_bff.vaccination_info.VaccinationInfoAnnouncement.AnnouncementIdentifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class collects all old vaccination info announcements and deletes this.
 *
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
@Slf4j
class VaccinationInfoAnnouncementDeleteJob {

	private final @NonNull VaccinationInfoAnnouncementRepository announcements;
	private final @NonNull Properties properties;

	@Transactional
	@Scheduled(cron = "${iris.client.vaccinfo.announcement.delete-cron:-}")
	void deleteAnnouncements() {

		var refDate = Instant.now().minus(properties.getDeleteAfter());

		var oldAnnouncements = announcements.findByMetadataCreatedIsBefore(refDate).toList();

		if (oldAnnouncements.isEmpty()) {
			return;
		}

		log.debug("{} vaccination info announcement(s) are deleted with duration {} after their creation!",
				oldAnnouncements.size(),
				properties.getDeleteAfter(),
				oldAnnouncements.get(0).getCreatedAt());

		announcements.deleteAll(oldAnnouncements);

		log.info("{} vaccination info announcement(s) (IDs: {}) were deleted with duration {} after their creation at {}!",
				oldAnnouncements.size(),
				oldAnnouncements.stream()
						.map(VaccinationInfoAnnouncement::getId)
						.map(AnnouncementIdentifier::toString)
						.collect(Collectors.joining(", ")),
				properties.getDeleteAfter(),
				oldAnnouncements.get(0).getCreatedAt());
	}

	@ConfigurationProperties("iris.client.vaccinfo.announcement")
	@ConstructorBinding
	@Value
	public static class Properties {

		/**
		 * Defines the {@link Duration} after that a vaccination info announcement will be deleted starting from the
		 * creation date.
		 */
		private final Duration deleteAfter;
	}
}
