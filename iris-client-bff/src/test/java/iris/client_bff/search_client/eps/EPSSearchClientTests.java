package iris.client_bff.search_client.eps;

import iris.client_bff.IrisWireMockTest;
import iris.client_bff.search_client.dto.LocationInformation;
import iris.client_bff.search_client.dto.LocationList;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.github.tomakehurst.wiremock.client.WireMock;

@IrisWireMockTest
@RequiredArgsConstructor
public class EPSSearchClientTests {

	private final EPSSearchClient systemUnderTest;

	private final String SEARCH_PATH = "/search/";
	private final String SEARCH_KEY = "Test";
	private final String PROVIDER_ID = "providerId";
	private final String LOCATION_ID = "locationId";
	private final String NAME = "name";

	@Autowired
	private ResourceLoader resourceLoader;

	@Test
	public void searchByKeywordReturnsLocation() throws IOException, IRISSearchException {
		// given
		String searchPath = SEARCH_PATH + SEARCH_KEY;

		Resource resource = resourceLoader
				.getResource("classpath:testdata/SearchClientSearchByKeywordReturnsLocation.json");
		InputStream jsonStream = resource.getInputStream();
		String json = new String(jsonStream.readAllBytes());

		var locItem = new LocationInformation().id(LOCATION_ID).name("name").providerId(PROVIDER_ID);
		var locList = new LocationList().addLocationsItem(locItem);

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(searchPath))
				.willReturn(
						WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(json)));

		// do
		LocationList searchResults = systemUnderTest.search(SEARCH_KEY);

		// then
		Assertions.assertNotNull(searchResults);
		Assertions.assertNotNull(searchResults.getLocations());
		Assertions.assertEquals(searchResults.getLocations().size(), locList.getLocations().size());
		Assertions.assertNotNull(searchResults.getLocations().get(0));
		Assertions.assertEquals(searchResults.getLocations().get(0).getId(), locItem.getId());
		Assertions.assertEquals(searchResults.getLocations().get(0).getName(), locItem.getName());
		Assertions.assertEquals(searchResults.getLocations().get(0).getProviderId(), locItem.getProviderId());
	}

	@Test
	public void searchByIdReturnsLocation() throws IOException, IRISSearchException {
		// given
		String searchPath = SEARCH_PATH + PROVIDER_ID + "/" + LOCATION_ID;
		Resource resource = resourceLoader.getResource("classpath:testdata/SearchClientSearchByIdReturnsLocation.json");
		InputStream jsonStream = resource.getInputStream();
		String json = new String(jsonStream.readAllBytes());

		var locItem = new LocationInformation().id(LOCATION_ID).name(NAME).providerId(PROVIDER_ID);

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(searchPath))
				.willReturn(
						WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(json)));

		// do
		var searchLocation = systemUnderTest.findByProviderIdAndLocationId(PROVIDER_ID, LOCATION_ID);

		// then
		Assertions.assertNotNull(searchLocation);
		Assertions.assertEquals(searchLocation.getId(), locItem.getId());
		Assertions.assertEquals(searchLocation.getProviderId(), locItem.getProviderId());
		Assertions.assertEquals(searchLocation.getName(), locItem.getName());
	}

	@Test
	public void searchByIdReturnsLocationWithoutLocationIdAndProviderId()
			throws IOException, IRISSearchException {
		// given
		String searchPath = SEARCH_PATH + PROVIDER_ID + "/" + LOCATION_ID;
		Resource resource = resourceLoader
				.getResource("classpath:testdata/SearchClientSearchByIdReturnsLocationWithoutLocationIdAndProviderId.json");
		InputStream jsonStream = resource.getInputStream();
		String json = new String(jsonStream.readAllBytes());

		var locItem = new LocationInformation().id(LOCATION_ID).name(NAME).providerId(PROVIDER_ID);

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(searchPath))
				.willReturn(
						WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(json)));

		// do
		var searchLocation = systemUnderTest.findByProviderIdAndLocationId(PROVIDER_ID, LOCATION_ID);

		// then
		Assertions.assertNotNull(searchLocation);
		Assertions.assertEquals(searchLocation.getId(), locItem.getId());
		Assertions.assertEquals(searchLocation.getProviderId(), locItem.getProviderId());
		Assertions.assertEquals(searchLocation.getName(), locItem.getName());
	}
}
