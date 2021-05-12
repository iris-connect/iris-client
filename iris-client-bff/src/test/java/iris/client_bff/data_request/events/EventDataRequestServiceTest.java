package iris.client_bff.data_request.events;

import iris.client_bff.config.IrisClientProperties;
import iris.client_bff.config.IrisProperties;
import iris.client_bff.data_request.DataRequest;
import iris.client_bff.search_client.SearchClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventDataRequestServiceTest {

	@Mock
	EventDataRequestRepository repository;

	@Mock
	RestTemplate restTemplate;

	@Mock
	IrisClientProperties irisClientProperties;

	@Mock
	IrisProperties irisProperties;

	@Mock
	ModelMapper modelMapper;

	@Mock
	SearchClient searchClient;

	EventDataRequestService service;

	@BeforeEach
	void setUp() {
		service = new EventDataRequestService(repository, restTemplate, irisClientProperties, irisProperties, modelMapper, searchClient);
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

		when(repository.getCountWithStatus(any(DataRequest.Status.class))).thenReturn(20);
		var status = DataRequest.Status.DATA_RECEIVED;
		var value = service.getCountWithStatus(status);

		verify(repository).getCountWithStatus(status);
		assertEquals(value, 20);
	}
}
