package iris.client_bff.feedback.web;

import iris.client_bff.feedback.FeedbackEndpointConnector;
import iris.client_bff.feedback.FeedbackResponseDto;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class provides an endpoint for post requests from Iris-Client-Frontend. This post requests contain feedback data
 * of users.
 *
 * @author Ostfalia Gruppe 12
 * @author Jens Kutzsche
 */
@RestController
@RequiredArgsConstructor
public class FeedbackRestAPI {

	private final FeedbackEndpointConnector connector;

	/**
	 * Post endpoint for feedback json data.
	 *
	 * @param sent feedback json of Iris-Client-Frontend
	 * @return response of the request to Iris-Public-Server
	 */
	@PostMapping(value = "/feedback")
	public ResponseEntity<FeedbackResponseDto> takeFeedbackPostRequest(@Valid @RequestBody FeedbackInputDto request) {
		return ResponseEntity.ok(connector.sendRequestToIrisPubServer(request));
	}
}
