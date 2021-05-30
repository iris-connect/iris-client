package iris.client_bff.feedback;

import iris.client_bff.config.IrisClientProperties;
import iris.client_bff.config.IrisProperties;
import iris.client_bff.feedback.web.DataFeedbackRequestDto;
import iris.client_bff.feedback.web.DataFeedbackResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequiredArgsConstructor
public class DataFeedbackEndpointConnector {

	private final @Qualifier("iris-rest") @NotNull RestTemplate rest;

	private final @NotNull IrisProperties properties;

	private final @NotNull IrisClientProperties clientProperties;

	/*
	 * Sends a post request with the given feedback data to Iris-Public-Server
	 * and returns the response of Iris-Public-Server
	 * @throws RestClientException is thrown if connection to Iris-Public-Server fails
	 * @param object containing feedback data which has to be sent to Iris-Public-Server
	 * @return object containing information about pushed git issue including git issue id
	 */
	public DataFeedbackResponseDto sendRequestToIrisPubServer(DataFeedbackRequestDto request) throws RestClientException {

		ResponseEntity<DataFeedbackResponseDto> response = rest.postForEntity(
			"https://{address}:{port}/feedback",
			request,
			DataFeedbackResponseDto.class,
			properties.getServerAddress().getHostName(),
			properties.getServerPort());

		log.trace("Submission Feedback - PUT to public server is sent: {}", "testid");
		var result = (DataFeedbackResponseDto) response.getBody();
		return result;
	}

}
