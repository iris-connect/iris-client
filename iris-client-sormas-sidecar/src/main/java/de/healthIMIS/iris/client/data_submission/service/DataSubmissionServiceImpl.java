package de.healthIMIS.iris.client.data_submission.service;

import de.healthIMIS.iris.client.core.IrisClientProperties;
import de.healthIMIS.iris.client.core.IrisProperties;
import de.healthIMIS.iris.client.data_submission.DataSubmissionDto;
import de.healthIMIS.iris.client.data_submission.entities.DataSubmission;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Slf4j
public class DataSubmissionServiceImpl implements DataSubmissionService {

    private final @NotNull RestTemplate rest;

    private final @NotNull IrisProperties properties;

    public DataSubmissionServiceImpl(@NotNull @Qualifier("iris-rest") RestTemplate rest, @NotNull IrisProperties properties) {
        this.rest = rest;
        this.properties = properties;
    }

    @Override
    public void deleteDataSubmissionFromServer(List<DataSubmissionDto> dataSubmissions) {

        dataSubmissions.forEach(submission -> {
            log.info("deleting submission "+submission.getId());
            callDeleteEndpoint(submission);
        });

    }

    private void callDeleteEndpoint(DataSubmissionDto dataSubmission) {

        rest.delete("https://{address}:{port}/hd/data-submissions/{dataSubmissionId}",
                properties.getServerAddress().getHostName(), properties.getServerPort(), dataSubmission.getId());

        log.info("Submission job - DELETE for "+dataSubmission.getId()+" sent to public server");
    }
}
