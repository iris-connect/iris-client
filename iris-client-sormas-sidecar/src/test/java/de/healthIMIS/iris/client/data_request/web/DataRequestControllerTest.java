package de.healthIMIS.iris.client.data_request.web;

import de.healthIMIS.iris.client.core.IrisClientProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.KeyStore;

@WebMvcTest(DataRequestController.class)
class DataRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IrisClientProperties irisClientProperties;

    @MockBean
    private KeyStore keyStore;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void test() {
    }

}