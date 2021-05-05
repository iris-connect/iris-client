package iris.client_bff.search_client.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationList;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IrisWebIntegrationTest
@RequiredArgsConstructor
class LocationSearchControllerIntegrationTests {

	private final MockMvc mvc;

	@MockBean
	private SearchClient searchClient;

	@Test
	public void endpointShouldBeProtected() throws Exception {

		given().mockMvc(mvc)
				.when().get("/search/{search_keyword}", "Test")
				.then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@WithMockUser
	public void serviceIsCalledForRequest() throws Exception {

		var searchString = "Test";

		var locItem = new LocationInformation().id("id").name("name").providerId("providerId");
		var locList = new LocationList().addLocationsItem(locItem);

		when(searchClient.search(anyString())).thenReturn(locList);

		given().mockMvc(mvc)
				.when().get("/search/{search_keyword}", searchString)
				.then()
				.statusCode(200)
				.body("locations[0].id", equalTo("id"))
				.body("locations[0].name", equalTo("name"))
				.body("locations[0].providerId", equalTo("providerId"));

		verify(searchClient).search(searchString);
	}

	@Test
	@WithMockUser
	public void error400_searchStringToShort() throws Exception {

		var searchString = "Tes";

		given().mockMvc(mvc)
				.when().get("/search/{search_keyword}", searchString)
				.then()
				.statusCode(400);
	}
}
