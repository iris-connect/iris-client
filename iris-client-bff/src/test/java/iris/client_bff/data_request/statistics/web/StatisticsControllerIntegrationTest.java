package iris.client_bff.data_request.statistics.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.data_request.DataRequest.Status;
import iris.client_bff.data_request.cases.IndexCaseService;
import iris.client_bff.data_request.events.EventDataRequestService;
import iris.client_bff.data_request.statistics.web.dto.StatisticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IrisWebIntegrationTest
public class StatisticsControllerIntegrationTest {
	private final String baseUrl = "/data-requests-client/statistics";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper om;

	@MockBean
	IndexCaseService indexCaseService;

	@MockBean
	EventDataRequestService eventService;

	// mock responses
	private final StatisticsDTO MOCK_STATISTICS_DTO = new StatisticsDTO(5, 10, 45);

	@BeforeEach
	void setUp() {
		when(indexCaseService.getCountSinceDate(any(Instant.class))).thenReturn(10);
		when(eventService.getCountSinceDate(any(Instant.class))).thenReturn(5);
		when(indexCaseService.getCountWithStatus(any(Status.class))).thenReturn(20);
		when(eventService.getCountWithStatus(any(Status.class))).thenReturn(25);
	}

	@Test
	void endpointShouldBeProtected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(status().isForbidden())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void getWeeklyData() throws Exception {

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(status().isOk())
				.andReturn();

		var stats = om.readValue(res.getResponse().getContentAsString(), StatisticsDTO.class);
		assertEquals(MOCK_STATISTICS_DTO, stats);
	}
}
