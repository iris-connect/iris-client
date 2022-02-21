package iris.client_bff.iris_messages.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.iris_messages.IrisMessageDataInitializer;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class IrisMessageControllerIntegrationTest {

  private final String baseUrl = "/iris-messages";

  private final MockMvc mockMvc;
  private final IrisMessageDataInitializer initializer;

  @Test
  @WithMockUser()
  @DisplayName("Tests getMessage to search with folder and search string")
  void getMessage_WithFolderAndSearchString_ReturnsMessage() throws Exception {

	given().mockMvc(mockMvc)
		.param("folder", initializer.getInboxFolder().getId().toString())
		.param("search", "First test")
		.when()
		.get(baseUrl)
		.then()
		.status(HttpStatus.OK)
		.contentType(ContentType.JSON)
		.body("content", hasSize(1))
		.body("content[0].subject", is("First test inbox subject"));
  }
}
