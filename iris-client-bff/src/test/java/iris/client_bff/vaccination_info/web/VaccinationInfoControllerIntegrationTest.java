package iris.client_bff.vaccination_info.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.text.IsBlankString.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.vaccination_info.VaccinationInfoRepository;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IrisWebIntegrationTest
@WithMockUser
@RequiredArgsConstructor
@Tag("vaccination-info")
@Tag("rest-controller")
@DisplayName("IT of web controller for vaccination info")
class VaccinationInfoControllerIntegrationTest {

	private static final String BASE_URL = "/vaccination-reports";
	private static final String DETAILS_URL = BASE_URL + "/{id}";

	final MockMvc mvc;
	final VaccinationInfoRepository vaccInfos;

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
				.status(FORBIDDEN);
	}

	@WithAnonymousUser
	@Test
	@DisplayName("getDetails: endpoint 🔒")
	void getDetails_EndpointProtected() {

		when()
				.get(DETAILS_URL, UUID.randomUUID())

				.then()
				.status(FORBIDDEN);
	}

	@Test
	@DisplayName("getVaccinationInfos: 🔙 all as overview")
	void getVaccinationInfos_ReturnsAll() {

		var count = vaccInfos.count();

		when()
				.get(BASE_URL)

				.then()
				.status(OK)
				.body("numberOfElements", is((int) count),
						"content.facility.name", everyItem(not(blankOrNullString())),
						"content.facility.address", everyItem(notNullValue()),
						"content.facility.contactPerson", everyItem(notNullValue()),
						"content.reportedAt", everyItem(notNullValue()),
						"content.vaccinationStatusCount.NOT_VACCINATED", everyItem(not(0)),
						"pageable.offset", is(0));
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
