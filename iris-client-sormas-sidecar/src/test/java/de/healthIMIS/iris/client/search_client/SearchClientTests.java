package de.healthIMIS.iris.client.search_client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import de.healthIMIS.iris.client.IrisWireMockTest;
import lombok.RequiredArgsConstructor;
import wiremock.com.fasterxml.jackson.core.JsonProcessingException;
import wiremock.com.fasterxml.jackson.databind.JsonMappingException;
import de.healthIMIS.iris.api.sidecarclient.model.LocationInformation;
import de.healthIMIS.iris.api.sidecarclient.model.LocationList;
import de.healthIMIS.iris.client.data_request.Location;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@IrisWireMockTest
@RequiredArgsConstructor
public class SearchClientTests {
	
	private final SearchClient systemUnderTest;

	private final String SEARCH_PATH = "/search/";
	private final String SEARCH_KEY = "Test";
	private final String PROVIDER_ID = "providerId";
	private final String LOCATION_ID = "locationId";
	private final String NAME = "name";
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Test
	public void searchByKeywordReturnsLocation() throws IOException {		
		// given
		String searchPath = SEARCH_PATH + SEARCH_KEY;
		
		Resource resource = resourceLoader.getResource("classpath:testdata/SearchClientSearchByKeywordReturnsLocation.json");
		InputStream jsonStream = resource.getInputStream();
		String json = new String(jsonStream.readAllBytes());
		
		var locItem = new LocationInformation().id(LOCATION_ID).name("name").providerId(PROVIDER_ID);
		var locList = new LocationList().addLocationsItem(locItem);
		
		stubFor(get(urlEqualTo(searchPath)).willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(json)));

		// do
		LocationList searchResults = systemUnderTest.search(SEARCH_KEY);

		// then
		assertNotNull(searchResults);
		assertNotNull(searchResults.getLocations());
		assertEquals(searchResults.getLocations().size(), locList.getLocations().size());
		assertNotNull(searchResults.getLocations().get(0));
		assertEquals(searchResults.getLocations().get(0).getId(), locItem.getId());
		assertEquals(searchResults.getLocations().get(0).getName(), locItem.getName());
		assertEquals(searchResults.getLocations().get(0).getProviderId(), locItem.getProviderId());
	}
	
	@Test
	public void searchByIdReturnsLocation() throws IOException {
		// given
		String searchPath = SEARCH_PATH + PROVIDER_ID + "/" + LOCATION_ID;		
		Resource resource = resourceLoader.getResource("classpath:testdata/SearchClientSearchByIdReturnsLocation.json");
		InputStream jsonStream = resource.getInputStream();
		String json = new String(jsonStream.readAllBytes());
		
		var locItem = new LocationInformation().id(LOCATION_ID).name(NAME).providerId(PROVIDER_ID);

		stubFor(get(urlEqualTo(searchPath)).willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(json)));
		
		// do
		Location searchLocation = systemUnderTest.findByProviderIdAndLocationId(PROVIDER_ID, LOCATION_ID);
		
		// then
		assertNotNull(searchLocation);
		assertEquals(searchLocation.getLocationId(), locItem.getId());
		assertEquals(searchLocation.getProviderId(), locItem.getProviderId());
		assertEquals(searchLocation.getName(), locItem.getName());
	}
	
	@Test
	public void searchByIdReturnsLocationWithoutLocationIdAndProviderId() throws IOException {
		// given
		String searchPath = SEARCH_PATH + PROVIDER_ID + "/" + LOCATION_ID;
		Resource resource = resourceLoader.getResource("classpath:testdata/SearchClientSearchByIdReturnsLocationWithoutLocationIdAndProviderId.json");
		InputStream jsonStream = resource.getInputStream();
		String json = new String(jsonStream.readAllBytes());
		
		var locItem = new LocationInformation().id(LOCATION_ID).name(NAME).providerId(PROVIDER_ID);

		stubFor(get(urlEqualTo(searchPath)).willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(json)));
		
		// do
		Location searchLocation = systemUnderTest.findByProviderIdAndLocationId(PROVIDER_ID, LOCATION_ID);
		
		// then
		assertNotNull(searchLocation);
		assertEquals(searchLocation.getLocationId(), locItem.getId());
		assertEquals(searchLocation.getProviderId(), locItem.getProviderId());
		assertEquals(searchLocation.getName(), locItem.getName());
	}
}

