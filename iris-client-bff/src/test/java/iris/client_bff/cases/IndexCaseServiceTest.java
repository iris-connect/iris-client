package iris.client_bff.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import iris.client_bff.cases.CaseDataRequest.Status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IndexCaseServiceTest {

	@Mock
	CaseDataRequestRepository repository;

	IndexCaseService service;

	@BeforeEach
	void setUp() {
		service = new IndexCaseService(repository);
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
