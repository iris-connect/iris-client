package iris.client_bff.search_client.eps;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.IrisWireMockTest;
import iris.client_bff.search_client.eps.dto.IdSearch;
import iris.client_bff.search_client.eps.dto.KeywordSearch;
import iris.client_bff.search_client.eps.dto.PageableDto;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.when;

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

	@MockBean
	private JsonRpcHttpClient rpcClient;

	@Test
	public void searchByKeywordReturnsLocation() throws Throwable {

		// given
		var mockedSearchResult = new LocationQueryResult().locations(List.of(new LocationInformation()
				.id(LOCATION_ID)
				.name(NAME)
				.providerId(PROVIDER_ID)))
				.totalElements(1)
				.page(0)
				.size(10);

		var payload = KeywordSearch.builder().searchKeyword(SEARCH_KEY).pageable(PageableDto.builder().build()).build();

		when(rpcClient.invoke(matches(".*\\.searchForLocation"), eq(payload), eq(LocationQueryResult.class)))
				.thenReturn(mockedSearchResult);

		// when
		LocationQueryResult actualSearchResults = systemUnderTest.search(SEARCH_KEY, Pageable.unpaged());

		// then
		assertEquals(mockedSearchResult, actualSearchResults);
	}

	@Test
	public void findByProviderIdAndLocationId() throws Throwable {

		// given
		var mockedSearchResult = new LocationInformation()
				.id(LOCATION_ID)
				.name(NAME)
				.providerId(PROVIDER_ID);

		var payload = IdSearch.builder().locationId(LOCATION_ID).providerId(PROVIDER_ID).build();

		when(rpcClient.invoke(matches(".*\\.getLocationDetails"), eq(payload), eq(LocationInformation.class)))
				.thenReturn(mockedSearchResult);

		// when
		var actualSearchResults = systemUnderTest.findByProviderIdAndLocationId(PROVIDER_ID, LOCATION_ID);

		// then
		assertEquals(mockedSearchResult, actualSearchResults);
	}

	@Test
	public void findByProviderIdAndLocationId_augmentedIds() throws Throwable {

		// given search response without provider id and location id
		var mockedSearchResult = new LocationInformation()
				.id(null)
				.name(NAME)
				.providerId(null);

		var payload = IdSearch.builder().locationId(LOCATION_ID).providerId(PROVIDER_ID).build();

		when(rpcClient.invoke(matches(".*\\.getLocationDetails"), eq(payload), eq(LocationInformation.class)))
				.thenReturn(mockedSearchResult);

		// when
		var actualSearchResults = systemUnderTest.findByProviderIdAndLocationId(PROVIDER_ID, LOCATION_ID);

		// then provider id and location id which were given in request are found in result

		var mockedSearchResultWithIds = new LocationInformation()
				.id(LOCATION_ID)
				.name(NAME)
				.providerId(PROVIDER_ID);

		assertEquals(mockedSearchResultWithIds, actualSearchResults);
	}
}
