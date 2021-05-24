package iris.client_bff.feedback_api;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/*
 * This class provides an endpoint for post requests from Iris-Client-Frontend.
 * This post requests contain feedback data of users.
 * @author Ostfalia Gruppe 12
 */
@RestController
public class FeedbackRestAPI {

	@Autowired
	DataFeedbackEndpointConnector requestManager;

	/*
	 * Post endpoint for feedback json data.
	 * @param sent feedback json of Iris-Client-Frontend
	 * @return response of the request to Iris-Public-Server
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/feedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DataFeedbackRequestDto> takeFeedbackPostRequest(@Valid @RequestBody DataFeedbackRequestDto request) {
		
		try {
			
			var responseIrisPubServer = requestManager.sendRequestToIrisPubServer(request);
			return ResponseEntity.ok(responseIrisPubServer);
			
		} catch (RestClientException e2) {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
