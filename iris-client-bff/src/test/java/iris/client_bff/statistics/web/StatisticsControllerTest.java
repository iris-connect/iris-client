package iris.client_bff.statistics.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequestService;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.statistics.web.dto.StatisticsDTO;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@IrisWebIntegrationTest
public class StatisticsControllerTest {
	private final String baseUrl = "/data-requests-client/statistics";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper om;

	@MockBean
    CaseDataRequestService caseDataRequestService;

	@MockBean
	EventDataRequestService eventService;

	// mock responses
	private final StatisticsDTO MOCK_STATISTICS_DTO = StatisticsDTO.builder()
			.indexCasesCount(5)
			.eventsCount(10)
			.sumStatus(45)
			.build();

	@BeforeEach
	void setUp() {
		when(caseDataRequestService.getCountSinceDate(any(Instant.class))).thenReturn(5);
		when(eventService.getCountSinceDate(any(Instant.class))).thenReturn(10);
		when(caseDataRequestService.getCountWithStatus(any(CaseDataRequest.Status.class))).thenReturn(20);
		when(eventService.getCountWithStatus(any(EventDataRequest.Status.class))).thenReturn(25);
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
