package iris.client_bff.iris_messages.eps;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageRepository;
import iris.client_bff.iris_messages.IrisMessageTestData;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.web.servlet.MockMvc;

@IrisWebIntegrationTest
@AllArgsConstructor
class IrisMessageDataControllerTest {

	private static final String JSON_RPC = "application/json-rpc";

	IrisMessageDataController dataController;
	IrisMessageRepository messageRepository;
	IrisMessageFolderRepository folderRepository;
	EPSIrisMessageClient messageClient;
	MessageSourceAccessor messages;
	MockMvc mvc;

	@Test
	void createIrisMessage() {

		IrisMessage localMessage = this.messageRepository.save(this.getMessage());
		IrisMessageTransferDto localMessageTransfer = IrisMessageTransferDto.fromEntity(localMessage);

		IrisMessageTransferDto remoteMessageTransfer = this.dataController.createIrisMessage(localMessageTransfer);

		assertNotNull(remoteMessageTransfer);
		assertThat(localMessageTransfer).isEqualTo(remoteMessageTransfer);
	}

	@Test
	void createIrisMessage_shouldFail_noData() {

		var e = assertThrows(IrisMessageException.class, () -> this.dataController.createIrisMessage(null));

		assertNotNull(e.getMessage());
		assertThat(e.getMessage()).contains(messages.getMessage("iris_message.invalid_id"));
	}

	@Test
	void createIrisMessage_shouldFail_invalidData() {

		IrisMessageTransferDto localMessageTransfer = Mockito.spy(IrisMessageTransferDto.fromEntity(this.getMessage()));

		localMessageTransfer.setSubject(IrisMessageTestData.INVALID_SUBJECT);
		verify(localMessageTransfer).setSubject(IrisMessageTestData.INVALID_SUBJECT);

		localMessageTransfer.setBody(IrisMessageTestData.INVALID_BODY);
		verify(localMessageTransfer).setBody(IrisMessageTestData.INVALID_BODY);

		var e = assertThrows(IrisMessageException.class,
				() -> this.dataController.createIrisMessage(localMessageTransfer));

		assertNotNull(e.getMessage());
		assertTrue(e.getMessage().contains(ErrorMessages.INVALID_INPUT));
	}

	@Test
	void createMessage_RealCommunucation() {

		var json = """
				{
				    "id":"1",
				    "jsonrpc":"2.0",
				    "method":"createIrisMessage",
				    "params":{
				    		"irisMessage":{
						        "hdAuthor":{
							        	"id":"hd-1",
							        	"name":"HD 1"
						       	},
										"hdRecipient":{
												"id":"hd-1",
												"name":"HD 1"
										},
										"subject":"Test Mail",
										"body":"This is a Test Mail"
								},
				        "_client":{"name":"hd-1"}
				    }
				}
				""";

		given().mockMvc(mvc)
				.contentType(ContentType.JSON)
				.body(json)

				.when()
				.post("/data-submission-rpc")

				.then()
				.status(OK)
				.contentType(JSON_RPC)
				.parser(JSON_RPC, Parser.JSON)
				.body("id", equalTo("1"))
				.body("result.subject", equalTo("Test Mail"));
	}

	private IrisMessage getMessage() {

		IrisMessageTestData testData = new IrisMessageTestData();

		Optional<IrisMessageFolder> outboxFolder = this.folderRepository
				.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);

		assertThat(outboxFolder).isPresent();

		IrisMessage message = testData.getTestOutboxMessage(outboxFolder.get());

		// for testing purposes, we use ourselves as message recipient
		message.setHdRecipient(this.messageClient.getOwnIrisMessageHdContact());

		return message;
	}
}
