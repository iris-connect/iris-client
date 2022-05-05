package iris.client_bff.vaccination_info;

import iris.client_bff.vaccination_info.VaccinationInfo.VaccinationInfoIdentifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class collects all old vaccination infos and deletes this.
 *
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
@Slf4j
class VaccinationInfoDeleteJob {

	private final @NonNull VaccinationInfoRepository vaccInfos;
	private final @NonNull Properties properties;

	@Transactional
	@Scheduled(cron = "${iris.client.vaccinfo.delete-cron:-}")
	void deleteAnnouncements() {

		var refDate = LocalDate.now().minus(properties.getDeleteAfter()).atStartOfDay().atZone(ZoneId.systemDefault())
				.toInstant();

		var oldAnnouncements = vaccInfos.findByMetadataCreatedIsBefore(refDate).toList();

		if (oldAnnouncements.isEmpty()) {
			return;
		}

		log.debug("{} vaccination info(s) are deleted with duration {} after their creation!",
				oldAnnouncements.size(),
				properties.getDeleteAfter(),
				oldAnnouncements.get(0).getCreatedAt());

		vaccInfos.deleteAll(oldAnnouncements);

		log.info("{} vaccination info(s) (IDs: {}) were deleted with duration {} after their creation at {}!",
				oldAnnouncements.size(),
				oldAnnouncements.stream()
						.map(VaccinationInfo::getId)
						.map(VaccinationInfoIdentifier::toString)
						.collect(Collectors.joining(", ")),
				properties.getDeleteAfter(),
				oldAnnouncements.get(0).getCreatedAt());
	}

	@ConfigurationProperties("iris.client.vaccinfo")
	@ConstructorBinding
	@Value
	static class Properties {

		/**
		 * Defines the {@link Period} after that a vaccination info will be deleted starting from the creation date.
		 */
		private final Period deleteAfter;
	}
}
