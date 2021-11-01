package iris.client_bff.cases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.core.token.TokenGenerator;
import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.proxy.ProxyServiceClient;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CaseDataRequestServiceTest {

	@Mock
	CaseDataRequestRepository repository;

	@Mock
	HibernateSearcher searcher;

	@Mock
	CaseEmailProvider emailProvider;

	@Mock
	ProxyServiceClient proxyServiceClient;

	TokenGenerator tokenGenerator;

	CaseDataRequestService service;

	@BeforeEach
	void setUp() {
		service = new CaseDataRequestService(repository, searcher, proxyServiceClient, tokenGenerator);
	}

	@Test
	void getCountSinceDate() {

		when(repository.getCountSinceDate(any(Instant.class))).thenReturn(10);
		var now = Instant.now();
		var value = service.getCountSinceDate(now);

		verify(repository).getCountSinceDate(now);
		assertEquals(10, value);
	}

	@Test
	void getCountWithStatus() {

		when(repository.getCountWithStatus(any(Status.class))).thenReturn(20);
		var status = Status.DATA_RECEIVED;
		var value = service.getCountWithStatus(status);

		verify(repository).getCountWithStatus(status);
		assertEquals(20, value);
	}
}
