package iris.client_bff.search_client.eps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.when;

import iris.client_bff.IrisWireMockTest;
import iris.client_bff.search_client.eps.dto.IdSearch;
import iris.client_bff.search_client.eps.dto.KeywordSearch;
import iris.client_bff.search_client.eps.dto.PageableDto;
import iris.client_bff.search_client.web.dto.LocationInformation;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@IrisWireMockTest
@RequiredArgsConstructor
class EPSSearchClientTests {

	private final EPSSearchClient systemUnderTest;

	private final String PROVIDER_ID = "providerId";
	private final String LOCATION_ID = "locationId";
	private final String NAME = "name";

	@MockBean
	@Qualifier("epsRpcClient")
	private JsonRpcHttpClient rpcClient;

	@Test
	void searchByKeywordReturnsLocation() throws Throwable {

		// given
		var mockedSearchResult = new LocationQueryResult().locations(List.of(new LocationInformation()
				.id(LOCATION_ID)
				.name(NAME)
				.providerId(PROVIDER_ID)))
				.totalElements(1)
				.page(0)
				.size(10);

		var payload = KeywordSearch.builder().searchKeyword("Test").pageable(PageableDto.builder().build()).build();

		when(rpcClient.invoke(matches(".*\\.searchForLocation"), eq(payload), eq(LocationQueryResult.class)))
				.thenReturn(mockedSearchResult);

		// when
		LocationQueryResult actualSearchResults = systemUnderTest.search("Test", Pageable.unpaged());

		// then
		assertEquals(mockedSearchResult, actualSearchResults);
	}

	@Test
	void findByProviderIdAndLocationId() throws Throwable {

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
	void findByProviderIdAndLocationId_augmentedIds() throws Throwable {

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
