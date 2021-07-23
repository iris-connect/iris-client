package iris.client_bff.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import iris.client_bff.config.DwConfig;
import iris.client_bff.config.HealthDepartmentConfig;
import iris.client_bff.proxy.ProxyServiceClient;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CaseDataRequestServiceTest {

	@Mock
	CaseDataRequestRepository repository;

	@Mock
	CaseEmailProvider emailProvider;

	@Mock
	ProxyServiceClient proxyServiceClient;

	DwConfig dwConfig;
	HealthDepartmentConfig hdConfig;

	CaseDataRequestService service;

	@BeforeEach
	void setUp() {
		service = new CaseDataRequestService(repository, emailProvider, proxyServiceClient, dwConfig, hdConfig);
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
		IndexCaseDetailsDTO updated = IndexCaseDetailsDTO.builder().build();
		IndexCaseStatusDTO status = IndexCaseStatusDTO.DATA_RECEIVED;

		service.sendDataRecievedEmail(updated, status);

		verify(emailProvider, times(1)).sendDataRecievedEmail(any());
	}

	@Test
	void sendDataRecievedEmailWithStatusNotUpdated() {
		IndexCaseDetailsDTO updated = IndexCaseDetailsDTO.builder().build();
		IndexCaseStatusDTO status = IndexCaseStatusDTO.DATA_REQUESTED;

		service.sendDataRecievedEmail(updated, status);

		verify(emailProvider, times(0)).sendDataRecievedEmail(any());
	}
}
