package de.healthIMIS.iris.client.data_submission.service;

import de.healthIMIS.iris.client.config.IrisProperties;
import de.healthIMIS.iris.client.data_submission.DataSubmissionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSubmissionServiceImpl implements DataSubmissionService {

	private final @Qualifier("iris-rest") @NotNull RestTemplate rest;

	private final @NotNull IrisProperties properties;

	@Override
	public void deleteDataSubmissionFromServer(List<DataSubmissionDto> dataSubmissions) {

		dataSubmissions.forEach(submission -> {
			log.info("deleting submission " + submission.getId());
			callDeleteEndpoint(submission);
		});

	}

	private void callDeleteEndpoint(DataSubmissionDto dataSubmission) {

		rest.delete("https://{address}:{port}/hd/data-submissions/{dataSubmissionId}",
				properties.getServerAddress().getHostName(), properties.getServerPort(), dataSubmission.getId());

		log.info("Submission job - DELETE for " + dataSubmission.getId() + " sent to public server");
	}
}
