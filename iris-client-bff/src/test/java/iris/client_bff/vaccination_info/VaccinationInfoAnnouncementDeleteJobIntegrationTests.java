package iris.client_bff.vaccination_info;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.utils.IrisDateTimeProvider;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class VaccinationInfoAnnouncementDeleteJobIntegrationTests {

	private final VaccinationInfoAnnouncementRepository announcements;
	private final IrisDateTimeProvider dateTimeProvider;
	private final VaccinationInfoAnnouncementDeleteJob deleteJob;
	private final Faker faker;

	@Test
	void testDeleteEventRequests() {

		var size = (int) announcements.count();

		// in time
		dateTimeProvider.setDelta(Duration.ofMinutes(-119));

		createAnnouncement();

		// to old
		dateTimeProvider.setDelta(Duration.ofMinutes(-121));

		var oldAnnouncement = createAnnouncement();

		dateTimeProvider.reset();

		assertThat(announcements.findAll())
				.hasSize(size + 2)
				.extracting(VaccinationInfoAnnouncement::getExternalId)
				.contains(oldAnnouncement.getExternalId());

		deleteJob.deleteAnnouncements();

		assertThat(announcements.findAll())
				.hasSize(size + 1)
				.extracting(VaccinationInfoAnnouncement::getExternalId)
				.doesNotContain(oldAnnouncement.getExternalId());
	}

	private VaccinationInfoAnnouncement createAnnouncement() {
		return announcements.save(VaccinationInfoAnnouncement.of(faker.idNumber().valid(), faker.idNumber().valid()));
	}
}
