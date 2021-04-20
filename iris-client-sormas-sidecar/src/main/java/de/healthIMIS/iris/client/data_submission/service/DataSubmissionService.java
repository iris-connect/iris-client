package de.healthIMIS.iris.client.data_submission.service;

import de.healthIMIS.iris.client.data_submission.DataSubmissionDto;

import java.util.List;

public interface DataSubmissionService {

    void deleteDataSubmissionFromServer(List<DataSubmissionDto> dataSubmissions);

}
