package iris.client_bff.feedback;

import iris.client_bff.config.BackendServiceProperties;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.feedback.web.FeedbackInputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.IJsonRpcClient;

/**
 * DataFeedbackEndpointConnector is responsible for sending a post encrypted request with feedback data to Iris-Public
 * Server.
 *
 * @author Ostfalia Gruppe 12
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackEndpointConnector {

	private final BackendServiceProperties config;
	private final IJsonRpcClient epsRpcClient;
	private final ModelMapper mapper;

	private String version = "";
	{
		try {
			var properties = PropertiesLoaderUtils.loadAllProperties("git.properties");
			version = properties.getProperty("git.build.version", "-") + " ("
					+ properties.getProperty("git.commit.id.abbrev", "-")
					+ ")";
		} catch (IOException e) {
			log.error("Can't load git properties", e);
		}
	}

	/*
	 * Sends a request with the given feedback data to Iris-Backend-Service
	 * and returns the response.
	 * @param object containing feedback data which has to be sent to Iris-Public-Server
	 * @return object containing information about pushed git issue including git issue id
	 */
	public FeedbackResponseDto sendRequestToIrisPubServer(FeedbackInputDto feedbackInput) {

		log.trace("Submission Feedback - feedback will be send to backend service");

		var feedback = mapper.map(feedbackInput, FeedbackOutputDto.class);
		feedback.setSourceApp(AlertService.APP);
		feedback.setAppVersion(version);

		var methodName = config.getEndpoint() + ".postFeedback";

		try {
			var result = epsRpcClient.invoke(methodName, Map.of("feedback", feedback), FeedbackResponseDto.class);

			log.debug("Submission Feedback - feedback was sent to backend service - ticket number = {}",
					result.getIssueNumber());

			return result;

		} catch (Throwable t) {

			log.error("Submission Feedback - could not be sent => Exception", t);

			throw new FeedbackSendingException(t);
		}
	}
}
