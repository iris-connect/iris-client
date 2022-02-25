package iris.client_bff.vaccination_info;

import iris.client_bff.DataInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(20)
class VaccinationInfoDataInitializer implements DataInitializer {

	static final String EXTERNAL_ID = "ec3ca901-762a-4832-be49-90d09d41ff94";
	static final String ANNOUNCEMENT_TOKEN = "dc9970d7-9cba-4830-8f30-0b63bbd21a47.proxy.dev.test-gesundheitsamt.de";

	private final VaccinationInfoAnnouncementRepository announcements;

	@Override
	public void initialize() {

		log.debug("Test data: creating data vaccination info announcements …");

		var announcement = VaccinationInfoAnnouncement.of(EXTERNAL_ID, ANNOUNCEMENT_TOKEN);

		announcements.save(announcement);
	}
}
