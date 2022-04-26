package iris.client_bff.vaccination_info.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.*;
import static org.hamcrest.text.IsBlankString.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.vaccination_info.VaccinationInfo;
import iris.client_bff.vaccination_info.VaccinationInfoDataInitializer;
import iris.client_bff.vaccination_info.VaccinationInfoRepository;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Locale;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@IrisWebIntegrationTest
@WithMockUser
// removes saved entities before this test so that other tests not affected this one
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@RequiredArgsConstructor
@TestInstance(Lifecycle.PER_METHOD)
@Tag("vaccination-info")
@Tag("rest-controller")
@DisplayName("IT of web controller for vaccination info")
class VaccinationInfoControllerIntegrationTest {

	private static final String BASE_URL = "/vaccination-reports";
	private static final String DETAILS_URL = BASE_URL + "/{id}";

	final MockMvc mvc;
	final VaccinationInfoRepository vaccInfos;

	@Autowired
	VaccinationInfoDataInitializer dataInitializer;

	@BeforeEach
	void init() {

		Locale.setDefault(Locale.ENGLISH);

		RestAssuredMockMvc.mockMvc(mvc);
	}

	@WithAnonymousUser
	@Test
	@DisplayName("getVaccinationInfos: endpoint 🔒")
	void getVaccinationInfos_EndpointProtected() {

		when()
				.get(BASE_URL)

				.then()
				.status(UNAUTHORIZED);
	}

	@WithAnonymousUser
	@Test
	@DisplayName("getDetails: endpoint 🔒")
	void getDetails_EndpointProtected() {

		when()
				.get(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(UNAUTHORIZED);
	}

	@Test
	@DisplayName("getVaccinationInfos: 🔙 all as overview")
	void getVaccinationInfos_ReturnsAll() {

		var count = vaccInfos.count();

		when()
				.get(BASE_URL)

				.then()
				.status(OK)
				.body("numberOfElements", is((int) count), getCommonChecks());
	}

	@Test
	@DisplayName("getVaccinationInfos: with search string ⇒ 🔙 filtered overview")
	void getVaccinationInfos_WithSearchString_ReturnsFiltered() {

		var path = BASE_URL + "?search={search}";

		when()
				.get(path, "For SearchingAB")

				.then()
				.status(OK)
				.body("numberOfElements", is(2), getCommonChecks());

		when()
				.get(path, "SearchingAB City")

				.then()
				.status(OK)
				.body("numberOfElements", is(1), getCommonChecks());
	}

	private Object[] getCommonChecks() {

		return new Object[] { "content.facility.name", everyItem(not(blankOrNullString())),
				"content.facility.address", everyItem(notNullValue()),
				"content.facility.contactPerson", everyItem(notNullValue()),
				"content.reportedAt", everyItem(notNullValue()),
				"content.vaccinationStatusCount.NOT_VACCINATED", everyItem(not(0)),
				"pageable.offset", is(0) };
	}

	@Test
	@DisplayName("getVaccinationInfos: with 'reportedAt' sorting ⇒ 🔙 sorted overview")
	void getVaccinationInfos_WithReportedAtSorting_ReturnsSorted() {

		var size = 2;

		var ids = dataInitializer.getInfoList().stream()
				.sorted(Comparator.comparing(VaccinationInfo::getCreatedAt).reversed())
				.map(this::getId)
				.limit(size)
				.toArray();

		when()
				.get(BASE_URL + "?size={size}&sort=reportedAt,desc", size)

				.then()
				.status(OK)
				.body("numberOfElements", is(size),
						"content.id", contains(ids));

		ids = dataInitializer.getInfoList().stream()
				.sorted(Comparator.comparing(VaccinationInfo::getCreatedAt))
				.map(this::getId)
				.limit(size)
				.toArray();

		when()
				.get(BASE_URL + "?size={size}&sort=reportedAt,asc", size)

				.then()
				.status(OK)
				.body("numberOfElements", is(size),
						"content.id", contains(ids));
	}

	@Test
	@DisplayName("getVaccinationInfos: with state 'NOT_VACCINATED' sorting ⇒ 🔙 sorted overview")
	void getVaccinationInfos_WithStateNotVaccinatedSorting_ReturnsSorted() {

		var size = 2;

		var infoList = dataInitializer.getInfoList();
		String[] ids = { getId(infoList.get(2)), getId(infoList.get(0)) };

		when()
				.get(BASE_URL + "?size={size}&sort=vaccinationStatusCount.notVaccinated,desc", size)

				.then()
				.status(OK)
				.body("numberOfElements", is(size),
						"content.id", contains(ids));

		ids = new String[] { getId(infoList.get(1)), getId(infoList.get(0)) };

		when()
				.get(BASE_URL + "?size={size}&sort=vaccinationStatusCount.notVaccinated,asc", size)

				.then()
				.status(OK)
				.body("numberOfElements", is(size),
						"content.id", contains(ids));
	}

	private String getId(VaccinationInfo vaccinationInfo) {
		return vaccinationInfo.getId().toString();
	}

	@Test
	@DisplayName("getVaccinationInfos: invalid search string ⇒ 🔙 validation errors")
	void getVaccinationInfos_WithInvalidSearchString_ReturnsValidationErrors() {

		when()
				.get(BASE_URL + "?search={search}", "@Search")

				.then()
				.status(BAD_REQUEST)
				.body(containsString("search: Contains illegal characters"));
	}

	@Test
	@DisplayName("getDetails: 🔙 one as details")
	@Transactional
	void getDetails_ReturnsDetails() {

		var vaccInfo = vaccInfos.findAll().get(0);
		int size = vaccInfo.getEmployees().size();

		when()
				.get(DETAILS_URL, vaccInfo.getId().toString())

				.then()
				.status(OK)
				.body(
						"facility.name", not(blankOrNullString()),
						"facility.address", notNullValue(),
						"facility.contactPerson", notNullValue(),
						"reportedAt", notNullValue(),
						"vaccinationStatusCount.NOT_VACCINATED", not(0),
						"employees.size()", is(size),
						"employees.lastName", everyItem(not(blankOrNullString())),
						"employees.address", everyItem(notNullValue()),
						"employees.vaccinationStatus", everyItem(is("NOT_VACCINATED")));
	}
}
