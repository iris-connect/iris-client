package de.healthIMIS.iris.client.data_request.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.data_request.Location;
import de.healthIMIS.iris.client.data_request.web.dto.ExistingDataRequestClientWithLocationList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
class DataRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;


    @MockBean
    private DataRequestManagement dataRequestManagement;

    @Test
    public void getDataRequests() throws Exception {
        var res = mockMvc.perform(MockMvcRequestBuilders.get("/data-requests-client/locations")
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        var dataRequests = om.readValue(res.getResponse().getContentAsString(), ExistingDataRequestClientWithLocationList.class);

        assertEquals(0, dataRequests.getDataRequests().size());

    }

    @Test
    public void getDataRequestByCode() throws Exception {
        postNewDataRequest();

        mockMvc.perform(MockMvcRequestBuilders.get("/data-requests-client/locations/123")
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void createDataRequest() throws Exception {
        postNewDataRequest();
    }

    private void postNewDataRequest() throws Exception {
        var dataRequest = Mockito.spy(new DataRequest("refId", "name", ZonedDateTime.now().toInstant(), ZonedDateTime.now().toInstant(), "requestDetails",
                "", new Location(), Sets.newSet(DataRequest.Feature.Guests)));

        // set by JPA on live calls, needs to be mocked here
        Mockito.doReturn(LocalDateTime.now()).when(dataRequest).getCreatedAt();
        Mockito.doReturn(LocalDateTime.now()).when(dataRequest).getLastModifiedAt();

        Mockito.when(
                dataRequestManagement.createLocationRequest(any(), any(), any(), any(), any(), any(), any())
        ).thenReturn(dataRequest);

        Mockito.when(
                dataRequestManagement.findById(anyString())
        ).thenReturn(Optional.of(dataRequest));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/data-requests-client/locations")
                        .content(TestData.DATA_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

}