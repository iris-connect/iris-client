package iris.client_bff.status.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.status.StatusService;
import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;
import iris.client_bff.status.eps.dto.ServerInfo;
import iris.client_bff.status.web.dto.AppStatus;
import iris.client_bff.status.web.dto.AppStatusInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IrisWebIntegrationTest
public class AppStatusControllerTest {
    private final String baseUrl = "/status/checkin-apps";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    StatusService statusService;

    private final DirectoryEntry MOCK_APP = mockApp();
    private final List<DirectoryEntry> MOCK_APP_LIST = List.of(MOCK_APP);

    private final AppStatusInfo MOCK_APP_STATUS_INFO_OK = mockAppStatusInfo("test.checkin-app.ok", AppStatus.Status.OK);
    private final AppStatusInfo MOCK_APP_STATUS_INFO_ERROR = mockAppStatusInfo("test.checkin-app.error", AppStatus.Status.ERROR);

    @Test
    public void endpointShouldBeProtected() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser()
    public void getApps() throws Exception {

        when(statusService.getApps()).thenReturn(MOCK_APP_LIST);

        var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(statusService).getApps();

        var list = om.readValue(res.getResponse().getContentAsString(), new TypeReference<List<DirectoryEntry>>(){});

        assertThat(list)
                .hasSize(1)
                .element(0)
                .extracting(DirectoryEntry::getName, DirectoryEntry::getGroups)
                .containsExactly(MOCK_APP.getName(), MOCK_APP.getGroups());

    }

    @Test
    @WithMockUser()
    public void getAppStatusInfo_shouldFail() throws Exception {

        when(statusService.getAppStatusInfo(anyString())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/invalid"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        verify(statusService).getAppStatusInfo(eq("invalid"));

    }

    @Test
    @WithMockUser()
    public void getAppStatusInfo_statusOK() throws Exception {

        when(statusService.getAppStatusInfo("test.checkin-app.ok")).thenReturn(MOCK_APP_STATUS_INFO_OK);

        var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/test.checkin-app.ok"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(statusService).getAppStatusInfo(eq("test.checkin-app.ok"));

        var appStatusInfo = om.readValue(res.getResponse().getContentAsString(), AppStatusInfo.class);
        assertEquals(appStatusInfo, MOCK_APP_STATUS_INFO_OK);
    }

    @Test
    @WithMockUser()
    public void getAppStatusInfo_statusError() throws Exception {

        when(statusService.getAppStatusInfo("test.checkin-app.error")).thenReturn(MOCK_APP_STATUS_INFO_ERROR);

        var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/test.checkin-app.error"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(statusService).getAppStatusInfo(eq("test.checkin-app.error"));

        var appStatusInfo = om.readValue(res.getResponse().getContentAsString(), AppStatusInfo.class);
        assertEquals(appStatusInfo, MOCK_APP_STATUS_INFO_ERROR);
    }

    private DirectoryEntry mockApp() {
        return DirectoryEntry.builder()
                .name("test-checkin-app")
                .groups(List.of("checkin-app"))
                .build();
    }

    private AppStatusInfo mockAppStatusInfo(String name, AppStatus.Status status) {
        ServerInfo info = ServerInfo.builder()
                .name(name)
                .build();
        Ping ping = Ping.builder()
                .serverInfo(info)
                .version("1.0.0")
                .build();
        return AppStatusInfo.builder()
                .info(ping)
                .status(status)
                .message("Test Status Message: " + status.toString())
                .build();
    }

}
