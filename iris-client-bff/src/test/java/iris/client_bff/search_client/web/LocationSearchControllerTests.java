package iris.client_bff.search_client.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import lombok.RequiredArgsConstructor;

import java.net.ConnectException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IrisWebIntegrationTest
@WithMockUser
@RequiredArgsConstructor
class LocationSearchControllerTests {

	private final MockMvc mvc;

	@MockBean
	private SearchClient searchClient;

	@BeforeEach
	void init() {
		RestAssuredMockMvc.mockMvc(mvc);
	}

	@Test
	@WithAnonymousUser
	void endpointShouldBeProtected() throws Exception {

		when()
				.get("/search/{search_keyword}", "Test")

				.then()
				.status(UNAUTHORIZED);
	}

	@Test
	void serviceIsCalledForRequest() throws Exception {

		var searchString = "Test";

		var locItem = new LocationInformation().id("id").name("name").providerId("providerId");
		var locList = new LocationQueryResult().addLocationsItem(locItem);

		when(searchClient.search(anyString(), any(Pageable.class))).thenReturn(locList);

		when()
				.get("/search/?search={search_keyword}", searchString)

				.then()
				.statusCode(200)
				.body("locations[0].id", equalTo("id"))
				.body("locations[0].name", equalTo("name"))
				.body("locations[0].providerId", equalTo("providerId"));

		verify(searchClient).search(eq(searchString), any(Pageable.class));
	}

	@Test
	void error400_searchStringToShort() throws Exception {

		var searchString = "Tes";

		when()
				.get("/search/?search={search_keyword}", searchString)

				.then()
				.statusCode(400);
	}

	@Test
	void error_serviceNotAvailable() throws Exception {

		var searchString = "Test";

		when(searchClient.search(anyString(), any(Pageable.class)))
				.thenThrow(new IRISSearchException(new ConnectException("Connection refused")));

		when()
				.get("/search/?search={search_keyword}", searchString)

				.then()
				.statusCode(503);
	}

	@Test
	void error400_searchStringWithForbiddenCharacter() throws Exception {

		var searchString = "+Test";

		when()
				.get("/search/?search={search_keyword}", searchString)

				.then()
				.statusCode(400);

		verifyNoInteractions(searchClient);
	}
}
