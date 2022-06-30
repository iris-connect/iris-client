package iris.client_bff.vaccination_info;

import static org.assertj.core.api.Assertions.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.database.IrisDateTimeProvider;
import iris.client_bff.core.model.Address;
import iris.client_bff.core.model.Sex;
import iris.client_bff.vaccination_info.VaccinationInfo.ContactPerson;
import iris.client_bff.vaccination_info.VaccinationInfo.Employee;
import iris.client_bff.vaccination_info.VaccinationInfo.Facility;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class VaccinationInfoDeleteJobIntegrationTests {

	private final VaccinationInfoRepository vaccInfos;
	private final IrisDateTimeProvider dateTimeProvider;
	private final VaccinationInfoDeleteJob deleteJob;
	private final Faker faker;

	@Test
	void testDeleteEventRequests() {

		var size = (int) vaccInfos.count();

		// in time
		dateTimeProvider.setDelta(Period.ofMonths(-6));

		createVaccinationInfo();

		// to old
		dateTimeProvider.setDelta(Period.ofMonths(-6).minusDays(1));

		var oldAnnouncement = createVaccinationInfo();

		dateTimeProvider.reset();

		assertThat(vaccInfos.findAll())
				.hasSize(size + 2)
				.extracting(VaccinationInfo::getExternalId)
				.contains(oldAnnouncement.getExternalId());

		deleteJob.deleteAnnouncements();

		assertThat(vaccInfos.findAll())
				.hasSize(size + 1)
				.extracting(VaccinationInfo::getExternalId)
				.doesNotContain(oldAnnouncement.getExternalId());
	}

	private VaccinationInfo createVaccinationInfo() {

		var address = Address.builder()
				.street(faker.address().streetName())
				.houseNumber(faker.address().buildingNumber())
				.city(faker.address().cityName())
				.zipCode(faker.address().zipCode())
				.build();

		var contactPerson = ContactPerson.of(
				faker.name().firstName(),
				faker.name().lastName(),
				faker.internet().emailAddress(),
				faker.phoneNumber().phoneNumber());

		var facility = Facility.of(faker.company().name(), address, contactPerson);

		var employee = Employee.of(
				faker.name().firstName(),
				faker.name().lastName(),
				LocalDate.now().minus(30, ChronoUnit.YEARS),
				Sex.UNKNOWN,
				address,
				faker.internet().emailAddress(),
				faker.phoneNumber().phoneNumber(),
				VaccinationType.COVID_19,
				VaccinationStatus.NOT_VACCINATED);

		return vaccInfos.save(VaccinationInfo.of(faker.idNumber().valid(), facility, Set.of(employee)));
	}
}
