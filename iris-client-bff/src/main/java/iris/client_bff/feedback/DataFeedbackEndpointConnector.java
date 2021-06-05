package iris.client_bff.feedback;

import iris.client_bff.feedback.web.DataFeedbackRequestDto;
import iris.client_bff.feedback.web.DataFeedbackResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/*
 * DataFeedbackEndpointConnector is responsible for sending a post encrypted
 * request with feedback data to Iris-Public Server.
 * @author Ostfalia Gruppe 12
 */
@Service
@Slf4j
public class DataFeedbackEndpointConnector {

	@Autowired
	private RestTemplate rest;

	/*
	 * Sends a post request with the given feedback data to Iris-Public-Server
	 * and returns the response of Iris-Public-Server
	 * @throws RestClientException is thrown if connection to Iris-Public-Server fails
	 * @param object containing feedback data which has to be sent to Iris-Public-Server
	 * @return object containing information about pushed git issue including git issue id
	 */
	public DataFeedbackResponseDto sendRequestToIrisPubServer(DataFeedbackRequestDto request) throws RestClientException {

		ResponseEntity<DataFeedbackResponseDto> response = rest.postForEntity(
			"https://localhost:8083/feedback",
			request,
			DataFeedbackResponseDto.class);

		log.trace("Submission Feedback - PUT to public server is sent: {}", "testid");
		var result = (DataFeedbackResponseDto) response.getBody();
		return result;
	}

}
