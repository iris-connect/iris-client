package iris.client_bff.status.web;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.status.StatusService;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@IrisWebIntegrationTest
@RequiredArgsConstructor
class AppStatusControllerTest {

	private final String baseUrl = "/status/checkin-apps";

	private final MockMvc mockMvc;
	private final ObjectMapper om;
	private final MessageSourceAccessor messages;

	@MockBean
	StatusService statusService;

	// private final DirectoryEntry MOCK_APP = mockApp();
	// private final List<DirectoryEntry> MOCK_APP_LIST = List.of(MOCK_APP);
	//
	// private final AppStatusInfo MOCK_APP_STATUS_INFO_OK = mockAppStatusInfo("test.checkin-app.ok", AppStatus.OK);
	// private final AppStatusInfo MOCK_APP_STATUS_INFO_ERROR = mockAppStatusInfo("test.checkin-app.error",
	// AppStatus.ACCESS_DENIED);

	@Test
	void endpointShouldBeProtected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	// @Test
	// @WithMockUser()
	// void getApps() throws Exception {
	//
	// when(statusService.getApps()).thenReturn(MOCK_APP_LIST);
	//
	// var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
	// .andExpect(MockMvcResultMatchers.status().isOk())
	// .andReturn();
	//
	// verify(statusService).getApps();
	//
	// var list = om.readValue(res.getResponse().getContentAsString(), new TypeReference<List<DirectoryEntry>>() {});
	//
	// assertThat(list)
	// .hasSize(1)
	// .element(0)
	// .extracting(DirectoryEntry::getName, DirectoryEntry::getGroups)
	// .containsExactly(MOCK_APP.getName(), MOCK_APP.getGroups());
	//
	// }
	//
	// @Test
	// @WithMockUser()
	// void getAppStatusInfo_shouldFail() throws Exception {
	//
	// when(statusService.getAppStatusInfo(anyString())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
	//
	// mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/invalid"))
	// .andExpect(MockMvcResultMatchers.status().is4xxClientError())
	// .andReturn();
	//
	// verify(statusService).getAppStatusInfo(eq("invalid"));
	//
	// }
	//
	// @Test
	// @WithMockUser()
	// void getAppStatusInfo_statusOK() throws Exception {
	//
	// when(statusService.getAppStatusInfo("test.checkin-app.ok")).thenReturn(MOCK_APP_STATUS_INFO_OK);
	//
	// given().mockMvc(mockMvc).header("Accept-Language", "en")
	// .when().get(baseUrl + "/test.checkin-app.ok")
	// .then()
	// .status(HttpStatus.OK)
	// .body("info.serverInfo.name", equalTo(MOCK_APP_STATUS_INFO_OK.getInfo().getServerInfo().getName()))
	// .body("info.version", equalTo(MOCK_APP_STATUS_INFO_OK.getInfo().getVersion()))
	// .body("status", equalTo(MOCK_APP_STATUS_INFO_OK.getStatus().name()))
	// .body("message", equalTo(messages.getMessage(MOCK_APP_STATUS_INFO_OK.getMessage(), Locale.ENGLISH)));
	//
	// verify(statusService).getAppStatusInfo(eq("test.checkin-app.ok"));
	// }
	//
	// @Test
	// @WithMockUser()
	// void getAppStatusInfo_statusError() throws Exception {
	//
	// when(statusService.getAppStatusInfo("test.checkin-app.error")).thenReturn(MOCK_APP_STATUS_INFO_ERROR);
	//
	// given().mockMvc(mockMvc).header("Accept-Language", "en")
	// .when().get(baseUrl + "/test.checkin-app.error")
	// .then()
	// .status(HttpStatus.OK)
	// .body("info.serverInfo.name", equalTo(MOCK_APP_STATUS_INFO_ERROR.getInfo().getServerInfo().getName()))
	// .body("info.version", equalTo(MOCK_APP_STATUS_INFO_ERROR.getInfo().getVersion()))
	// .body("status", equalTo(MOCK_APP_STATUS_INFO_ERROR.getStatus().name()))
	// .body("message", equalTo(messages.getMessage(MOCK_APP_STATUS_INFO_ERROR.getMessage(), Locale.ENGLISH)));
	//
	// verify(statusService).getAppStatusInfo(eq("test.checkin-app.error"));
	// }
	//
	// private DirectoryEntry mockApp() {
	// return DirectoryEntry.builder()
	// .name("test-checkin-app")
	// .groups(List.of("checkin-app"))
	// .build();
	// }
	//
	// private AppStatusInfo mockAppStatusInfo(String name, AppStatus status) {
	// ServerInfo info = ServerInfo.builder()
	// .name(name)
	// .build();
	// AppInfo ping = AppInfo.builder()
	// .serverInfo(info)
	// .version("1.0.0")
	// .build();
	// return AppStatusInfo.builder()
	// .info(ping)
	// .appStatus(status)
	// .build();
	// }
}
