package iris.client_bff.events.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.RestResponsePage;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.EventStatusDTO;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IrisWebIntegrationTest
class EventDataRequestControllerIntegrationTest {

	private final String baseUrl = "/data-requests-client/events";

	TypeReference<RestResponsePage<ExistingDataRequestClientWithLocation>> PAGE_TYPE = new TypeReference<>() {};

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper om;

	@MockBean
	private EventDataRequestService dataRequestManagement;

	private final EventDataRequest MOCK_EVENT = getEventDataRequest();
	private final Page<EventDataRequest> eventsPage = new RestResponsePage<>(List.of(MOCK_EVENT));

	@BeforeEach
	void setUp() {
		when(dataRequestManagement.findAll(any(Pageable.class))).thenReturn(eventsPage);

		when(dataRequestManagement.findByStatus(any(Status.class), any(Pageable.class)))
				.thenReturn(eventsPage);

		when(dataRequestManagement.searchByRefIdOrName(any(String.class), any(Pageable.class)))
				.thenReturn(eventsPage);

		when(dataRequestManagement.findByStatusAndSearchByRefIdOrName(any(Status.class), any(String.class),
				any(Pageable.class)))
						.thenReturn(eventsPage);
	}

	@Test
	public void endpointShouldBeProtected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@WithMockUser()
	public void getDataRequests() throws Exception {
		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(dataRequestManagement).findAll(any(Pageable.class));

		var dataRequests = om.readValue(res.getResponse().getContentAsString(),
				PAGE_TYPE);

		assertEquals(1, dataRequests.getContent().size());
	}

	@Test
	@WithMockUser()
	public void getAllWithStatus() throws Exception {
		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?status=DATA_REQUESTED"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(dataRequestManagement).findByStatus(eq(Status.DATA_REQUESTED), any(Pageable.class));

		var dataRequests = om.readValue(res.getResponse().getContentAsString(),
				PAGE_TYPE);

		assertEquals(1, dataRequests.getContent().size());
	}

	@Test
	@WithMockUser()
	public void getAllFiltered() throws Exception {
		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?search=test"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(dataRequestManagement).searchByRefIdOrName(eq("test"), any(Pageable.class));

		var dataRequests = om.readValue(res.getResponse().getContentAsString(),
				PAGE_TYPE);

		assertEquals(1, dataRequests.getContent().size());
	}

	@Test
	@WithMockUser()
	public void getAllWithStatusAndFiltered() throws Exception {
		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?search=test&status=DATA_REQUESTED"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(dataRequestManagement).findByStatusAndSearchByRefIdOrName(eq(Status.DATA_REQUESTED), eq("test"),
				any(Pageable.class));

		var dataRequests = om.readValue(res.getResponse().getContentAsString(),
				PAGE_TYPE);

		assertEquals(1, dataRequests.getContent().size());
	}

	@Test
	@WithMockUser()
	public void getDataRequestByCode() throws Exception {
		postNewDataRequest();

		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/123"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@WithMockUser()
	public void updateDataRequestByCode() throws Exception {
		String REFID = "refId";
		var dataRequest = Mockito.spy(
				new EventDataRequest(
						REFID,
						"name",
						Instant.now(),
						Instant.now(),
						"",
						"requestDetails",
						"",
						new Location()));

		var dataRequestUpdated = new EventDataRequest(
				REFID,
				"name",
				Instant.now(),
				Instant.now(),
				"This is a test comment",
				"requestDetails",
				"",
				new Location());
		dataRequestUpdated.setComment("Different test comment");
		dataRequestUpdated.setName("NameSecond");
		dataRequestUpdated.setRefId("refIdSecond");
		dataRequestUpdated.setStatus(Status.ABORTED);

		Mockito.when(dataRequestManagement.findById(anyString())).thenReturn(Optional.of(dataRequest));

		EventUpdateDTO patch = EventUpdateDTO.builder()
				.name("new name")
				.externalRequestId("new external ref id")
				.comment("a great new comment")
				.status(EventStatusDTO.ABORTED)
				.build();
		var patchBody = om.writeValueAsString(patch);

		Mockito.when(dataRequestManagement.update(dataRequest, patch)).thenReturn(dataRequestUpdated);

		mockMvc.perform(MockMvcRequestBuilders
				.patch("/data-requests-client/events/123")
				.content(patchBody)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@WithMockUser()
	public void createDataRequest() throws Exception {
		postNewDataRequest();
	}

	private void postNewDataRequest() throws Exception {
		var dataRequest = Mockito.spy(
				new EventDataRequest(
						"refId",
						"name",
						Instant.now(),
						Instant.now(),
						"This is a test comment",
						"requestDetails",
						"",
						new Location()));

		// set by JPA on live calls, needs to be mocked here
		Mockito.doReturn(Instant.now()).when(dataRequest).getCreatedAt();
		Mockito.doReturn(Instant.now()).when(dataRequest).getLastModifiedAt();

		Mockito.when(dataRequestManagement.createLocationRequest(any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(dataRequest);

		Mockito.when(dataRequestManagement.findById(anyString())).thenReturn(Optional.of(dataRequest));

		mockMvc
				.perform(
						MockMvcRequestBuilders.post(baseUrl).content(TestData.DATA_REQUEST)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	private EventDataRequest getEventDataRequest() {
		return new EventDataRequest(
				"refId",
				"name",
				Instant.now(),
				Instant.now(),
				"",
				"requestDetails",
				"",
				getTestLocation());
	}

	private Location getTestLocation() {
		Location location = new Location();
		location.setId(new Location.LocationIdentifier());
		location.setName("FC Darmstadt");
		location.setContactAddressCity("Hintertupfingen");
		location.setContactAddressStreet("Testallee 42");
		location.setContactAddressZip("12345");
		location.setContactEmail("tim@smartmeeting.online");
		location.setContactOfficialName("Fussballclub Darmstadt");
		location.setContactOwnerEmail("tim@smartmeeting.online");
		location.setContactPhone("0151 47110815");
		location.setContactRepresentative("Hans Mueller");
		location.setLocationId("702830d0-7665-400e-821e-1cef4df4c792");
		location.setProviderId("b220f816-d850-4260-b988-ef0ae171a498");
		return location;
	}
}
