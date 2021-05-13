package iris.client_bff.events.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.model.Location;
import iris.client_bff.events.web.dto.EventStatusDTO;
import iris.client_bff.events.web.dto.EventUpdateDTO;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocationList;

import java.time.Instant;
import java.util.Optional;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@IrisWebIntegrationTest
class EventDataRequestControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper om;

  @MockBean
  private EventDataRequestService dataRequestManagement;

  @Test
  public void endpointShouldBeProtected() throws Exception {
	mockMvc.perform(MockMvcRequestBuilders.get("/data-requests-client/events"))
		.andExpect(MockMvcResultMatchers.status().isForbidden())
		.andReturn();
  }

  @Test
  @WithMockUser()
  public void getDataRequests() throws Exception {
	var res = mockMvc.perform(MockMvcRequestBuilders.get("/data-requests-client/events"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();

	var dataRequests = om.readValue(res.getResponse().getContentAsString(),
		ExistingDataRequestClientWithLocationList.class);

	assertEquals(0, dataRequests.getDataRequests().size());

  }

  @Test
  @WithMockUser()
  public void getDataRequestByCode() throws Exception {
	postNewDataRequest();

	mockMvc.perform(MockMvcRequestBuilders.get("/data-requests-client/events/123"))
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

	Mockito.when(dataRequestManagement.findById(any(UUID.class))).thenReturn(Optional.of(dataRequest));

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

	Mockito.when(dataRequestManagement.createDataRequest(any(), any(), any(), any(), any(), any(), any(), any(), any()))
		.thenReturn(dataRequest);

	Mockito.when(dataRequestManagement.findById(any(UUID.class))).thenReturn(Optional.of(dataRequest));

	mockMvc
		.perform(
			MockMvcRequestBuilders.post("/data-requests-client/events").content(TestData.DATA_REQUEST)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
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
