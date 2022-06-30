package iris.client_bff.status.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.WithMockIrisUser;
import iris.client_bff.status.AppInfo;
import iris.client_bff.status.AppStatus;
import iris.client_bff.status.Apps;
import iris.client_bff.status.Apps.App;
import iris.client_bff.status.StatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@IrisWebIntegrationTest
@WithMockIrisUser
@RequiredArgsConstructor
class AppStatusControllerTest {

	private final String baseUrl = "/status/checkin-apps";

	private final MockMvc mockMvc;
	private final ObjectMapper om;
	private final MessageSourceAccessor messages;

	@MockBean
	StatusService statusService;

	private final App MOCK_APP = mockApp();
	private final Apps MOCK_APPS = new Apps(List.of(MOCK_APP));

	private final AppInfo MOCK_APP_INFO_OK = mockAppInfo("test.checkin-app.ok", AppStatus.OK);
	private final AppInfo MOCK_APP_INFO_ERROR = mockAppInfo("test.checkin-app.error",
			AppStatus.ACCESS_DENIED);

	@Test
	@WithAnonymousUser
	void endpointShouldBeProtected() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void getApps() throws Exception {

		when(statusService.getApps()).thenReturn(MOCK_APPS);

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
				.andExpect(status().isOk())
				.andReturn();

		verify(statusService).getApps();

		var list = om.readValue(res.getResponse().getContentAsString(), new TypeReference<List<App>>() {});

		assertThat(list)
				.hasSize(1)
				.element(0)
				.extracting(App::name)
				.isEqualTo(MOCK_APP.name());
	}

	@Test
	void getAppStatusInfo_shouldFail() throws Exception {

		when(statusService.getAppInfo(anyString())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/invalid"))
				.andExpect(status().is4xxClientError());

		verify(statusService).getAppInfo(eq("invalid"));
	}

	@Test
	void getAppStatusInfo_forbiddenCharacter_shouldFail() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/+invalid"))
				.andExpect(status().is4xxClientError());

		verifyNoInteractions(statusService);
	}

	@Test
	void getAppStatusInfo_statusOK() throws Exception {

		when(statusService.getAppInfo("test.checkin-app.ok")).thenReturn(MOCK_APP_INFO_OK);

		given().mockMvc(mockMvc).header("Accept-Language", "en")
				.when().get(baseUrl + "/test.checkin-app.ok")
				.then()
				.status(HttpStatus.OK)
				.body("info.name", equalTo(MOCK_APP_INFO_OK.getName()))
				.body("info.version", equalTo(MOCK_APP_INFO_OK.getVersion()))
				.body("status", equalTo(MOCK_APP_INFO_OK.getStatus().getStatus().name()))
				.body("message", equalTo(messages.getMessage(MOCK_APP_INFO_OK.getStatus(), Locale.ENGLISH)));

		verify(statusService).getAppInfo(eq("test.checkin-app.ok"));
	}

	@Test
	void getAppStatusInfo_statusError() throws Exception {

		when(statusService.getAppInfo("test.checkin-app.error")).thenReturn(MOCK_APP_INFO_ERROR);

		given().mockMvc(mockMvc).header("Accept-Language", "en")
				.when().get(baseUrl + "/test.checkin-app.error")
				.then()
				.status(HttpStatus.OK)
				.body("info.name", equalTo(MOCK_APP_INFO_ERROR.getName()))
				.body("info.version", equalTo(MOCK_APP_INFO_ERROR.getVersion()))
				.body("status", equalTo(MOCK_APP_INFO_ERROR.getStatus().getStatus().name()))
				.body("message", equalTo(messages.getMessage(MOCK_APP_INFO_ERROR.getStatus(), Locale.ENGLISH)));

		verify(statusService).getAppInfo(eq("test.checkin-app.error"));
	}

	private App mockApp() {
		return new App("test-checkin-app");
	}

	private AppInfo mockAppInfo(String name, AppStatus status) {
		return new AppInfo(name, "1.0.0").setStatus(status);
	}
}
