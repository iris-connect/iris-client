package iris.client_bff.vaccination_info;

import static java.time.temporal.ChronoUnit.*;

import iris.client_bff.DataInitializer;
import iris.client_bff.core.Sex;
import iris.client_bff.core.model.Address;
import iris.client_bff.vaccination_info.VaccinationInfo.ContactPerson;
import iris.client_bff.vaccination_info.VaccinationInfo.Employee;
import iris.client_bff.vaccination_info.VaccinationInfo.Facility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(20)
class VaccinationInfoDataInitializer implements DataInitializer {

	static final String EXTERNAL_ID = "ec3ca901-762a-4832-be49-90d09d41ff94";
	static final String ANNOUNCEMENT_TOKEN = "dc9970d7-9cba-4830-8f30-0b63bbd21a47.proxy.dev.test-gesundheitsamt.de";

	private final VaccinationInfoAnnouncementRepository announcements;
	private final VaccinationInfoRepository vaccInfos;

	private Faker faker = Faker.instance(Locale.GERMANY);

	@Override
	public void initialize() {

		log.debug("Test data: creating data vaccination info announcements …");

		var announcement = VaccinationInfoAnnouncement.of(EXTERNAL_ID, ANNOUNCEMENT_TOKEN);

		announcements.save(announcement);

		vaccInfos.save(VaccinationInfo.of(EXTERNAL_ID, createFacility(), createEmployees(3)));
		vaccInfos.save(VaccinationInfo.of(EXTERNAL_ID, createFacility(), createEmployees(1)));
		vaccInfos.save(VaccinationInfo.of(EXTERNAL_ID, createFacility(), createEmployees(5)));
	}

	private Facility createFacility() {

		return Facility.of(
				faker.company().name(),
				createAddress(),
				createPerson());
	}

	private Address createAddress() {

		var addressFaker = faker.address();

		return Address.builder()
				.street(addressFaker.streetName())
				.houseNumber(addressFaker.buildingNumber())
				.zipCode(addressFaker.zipCode())
				.city(addressFaker.cityName())
				.build();
	}

	private ContactPerson createPerson() {

		return ContactPerson.of(
				faker.name().firstName(),
				faker.name().lastName(),
				faker.internet().emailAddress(),
				faker.phoneNumber().phoneNumber());
	}

	private Set<Employee> createEmployees(int count) {

		return Stream.generate(this::createEmployee)
				.limit(count)
				.collect(Collectors.toSet());
	}

	private Employee createEmployee() {

		return Employee.of(
				faker.name().firstName(),
				faker.name().lastName(),
				LocalDate.now().minus(RandomUtils.nextInt(20, 60), YEARS),
				Sex.UNKNOWN,
				createAddress(),
				faker.internet().emailAddress(),
				faker.phoneNumber().phoneNumber(),
				VaccinationType.COVID_19,
				VaccinationStatus.NOT_VACCINATED);
	}
}
