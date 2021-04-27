package de.healthIMIS.iris.client.data_submission.supplier_connection;

import de.healthIMIS.iris.client.config.IrisClientProperties;
import de.healthIMIS.iris.client.config.IrisProperties;
import de.healthIMIS.iris.client.data_submission.DataSubmissionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSubmissionEndpointConnector {

	private final @Qualifier("iris-rest") @NotNull RestTemplate rest;

	private final @NotNull IrisProperties properties;
	private final @NotNull IrisClientProperties clientProperties;

	public FetchedDataSubmissions fetchDataSubmissions(Instant lastSync) {

		var response = rest.getForEntity(
				"http://{address}:{port}/hd/data-submissions?departmentId={depId}&from={from}",
				DataSubmissionDto[].class, properties.getServerAddress().getHostName(), properties.getServerPort(),
				clientProperties.getClientId(), lastSync);

		var dataSubmissions = getDataSubmissionsFrom(response);
		var lastModified = Instant.ofEpochMilli(response.getHeaders().getLastModified());

		return FetchedDataSubmissions.of(dataSubmissions, lastModified);
	}

	public void deleteDataSubmissionFromServer(List<DataSubmissionDto> dataSubmissions) {

		dataSubmissions.forEach(submission -> {
			log.info("deleting submission " + submission.getId());
			callDeleteEndpoint(submission);
		});

	}

	private List<DataSubmissionDto> getDataSubmissionsFrom(ResponseEntity<DataSubmissionDto[]> response) {
		return response.getBody() != null ? Arrays.asList(response.getBody()) : List.<DataSubmissionDto> of();
	}

	private void callDeleteEndpoint(DataSubmissionDto dataSubmission) {

		rest.delete("https://{address}:{port}/hd/data-submissions/{dataSubmissionId}",
				properties.getServerAddress().getHostName(), properties.getServerPort(), dataSubmission.getId());

		log.info("Submission job - DELETE for " + dataSubmission.getId() + " sent to public server");
	}
}
