package de.healthIMIS.iris.client.data_request.web;

import de.healthIMIS.iris.client.data_request.DataRequest;
import de.healthIMIS.iris.client.data_request.DataRequestManagement;
import de.healthIMIS.iris.client.data_request.Location;
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

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
class DataRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataRequestManagement dataRequestManagement;

    @Test
    public void getDataRequests() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/data-requests-client/locations")
        ).andExpect(MockMvcResultMatchers.status().isNotImplemented()).andReturn();
    }

    @Test
    public void getDataRequestByCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/data-requests-client/locations/123")
        ).andExpect(MockMvcResultMatchers.status().isNotImplemented()).andReturn();
    }

    @Test
    public void createDataRequest() throws Exception {

        var dataRequest = new DataRequest("refId", "name", ZonedDateTime.now().toInstant(), ZonedDateTime.now().toInstant(), "requestDetails",
                "", new Location(), Sets.newSet(DataRequest.Feature.Guests));



        Mockito.when(
                dataRequestManagement.createLocationRequest(any(), any(), any(), any(), any(), any())
        ).thenReturn(dataRequest);


        mockMvc.perform(
                MockMvcRequestBuilders.post("/data-requests-client/locations")
                        .content(TestData.DATA_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotImplemented()).andReturn();
    }

}