/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package iris.client_bff.web.filter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.googlecode.jsonrpc4j.ErrorResolver.JsonError;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@TestPropertySource(properties = { "iris.suspiciously.request.defaults.content-length-blocking-size=15B",
		"iris.suspiciously.request.for-uri.data-submission-rpc.content-length-blocking-size=15B" })
@RequiredArgsConstructor
class ApplicationRequestSizeLimitFilterTests {

	private final MockMvc mvc;

	@MockBean
	AlertService alertService;

	@Test
	@WithMockUser()
	void requestTooLarge_RestEndpoint() throws Exception {

		mvc.perform(post("/data-requests-client/events")
				.content("Test Content ABCDEFGHIJK")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isPayloadTooLarge());

		verify(alertService).createAlertTicketAndMessage(any(String.class), any(String.class));
	}

	@Test
	@WithMockUser()
	void requestTooLarge_JsonRpc() throws Exception {

		var content = mvc.perform(post("/data-submission-rpc")
				.content("Test Content ABCDEFGHIJK")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

		assertThat(content).contains("-00001").contains(ErrorMessages.REQUEST_TOO_LARGE);

		verify(alertService).createAlertTicketAndMessage(any(String.class), any(String.class));
	}

	@Test
	@WithMockUser()
	void wrongContentType_JsonRpc() throws Exception {

		var content = mvc.perform(post("/data-submission-rpc")
				.content("<x>Content</x>")
				.contentType(MediaType.TEXT_XML))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

		assertThat(content).contains(Integer.toString(JsonError.PARSE_ERROR.code)).contains(JsonError.PARSE_ERROR.message);
	}
}
