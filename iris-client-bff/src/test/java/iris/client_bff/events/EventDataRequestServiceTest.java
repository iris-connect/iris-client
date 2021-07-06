package iris.client_bff.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.eps.DataProviderClient;
import iris.client_bff.events.web.dto.EventStatusDTO;
import iris.client_bff.proxy.ProxyServiceClient;
import iris.client_bff.search_client.SearchClient;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class EventDataRequestServiceTest {

	@Mock
	EventDataRequestRepository repository;

	@Mock
	ModelMapper modelMapper;

	@Mock
	SearchClient searchClient;

	EventDataRequestService service;

	@Mock
	DataProviderClient epsDataRequestClient;

	@Mock
	ProxyServiceClient proxyServiceClient;

	@Mock
	EventEmailProvider emailProvider;

	@BeforeEach
	void setUp() {
		service = new EventDataRequestService(repository, searchClient, proxyServiceClient, epsDataRequestClient, emailProvider);
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

	@Test
	void sendDataRecievedEmailWithStatusUpdated() {
		EventDataRequest updated = new EventDataRequest();
		EventStatusDTO status = EventStatusDTO.DATA_RECEIVED;

		service.sendDataRecievedEmail(updated, status);

		verify(emailProvider, times(1)).sendDataRecievedEmailAsynchroniously(any());
	}

	@Test
	void sendDataRecievedEmailWithStatusNotUpdated() {
		EventDataRequest updated = new EventDataRequest();
		EventStatusDTO status = EventStatusDTO.DATA_REQUESTED;

		service.sendDataRecievedEmail(updated, status);

		verify(emailProvider, times(0)).sendDataRecievedEmailAsynchroniously(any());
	}
}
