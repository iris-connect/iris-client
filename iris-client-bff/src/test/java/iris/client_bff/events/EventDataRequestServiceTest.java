package iris.client_bff.events;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.core.database.HibernateSearcher;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.eps.DataProviderClient;
import iris.client_bff.proxy.ProxyServiceClient;
import iris.client_bff.search_client.SearchClient;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventDataRequestServiceTest {

	@Mock
	EventDataRequestRepository repository;

	@Mock
	HibernateSearcher searcher;

	@Mock
	SearchClient searchClient;

	@Mock
	DataProviderClient epsDataRequestClient;

	@Mock
	ProxyServiceClient proxyServiceClient;

	@Mock
	EventEmailProvider emailProvider;

	EventDataRequestService service;

	@BeforeEach
	void setUp() {

		service = new EventDataRequestService(repository, searcher, searchClient, proxyServiceClient, epsDataRequestClient,
				new LocationMapperImpl());
	}

	@Test
	void getCountSinceDate() {

		when(repository.getCountSinceDate(any(Instant.class))).thenReturn(10);
		var now = Instant.now();
		var value = service.getCountSinceDate(now);

		verify(repository).getCountSinceDate(now);
		assertEquals(value, 10);
	}

	@Test
	void getCountWithStatus() {

		when(repository.getCountWithStatus(any(Status.class))).thenReturn(20);
		var status = Status.DATA_RECEIVED;
		var value = service.getCountWithStatus(status);

		verify(repository).getCountWithStatus(status);
		assertEquals(value, 20);
	}
}
