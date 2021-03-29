package de.healthIMIS.iris.client.data_request.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
class DataRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        mockMvc.perform(
                MockMvcRequestBuilders.post("/data-requests-client/locations")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotImplemented()).andReturn();
    }

}