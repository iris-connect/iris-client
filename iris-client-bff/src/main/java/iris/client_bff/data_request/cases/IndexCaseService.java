package iris.client_bff.data_request.cases;

import iris.client_bff.data_request.DataRequest.DataRequestIdentifier;
import iris.client_bff.data_request.DataRequest.Status;
import iris.client_bff.data_request.cases.web.dto.IndexCaseInsertDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class IndexCaseService {

	CaseDataRequestRepository repository;

	public List<CaseDataRequest> findAll() {
		Spliterator<CaseDataRequest> all = repository.findAll().spliterator();
		return StreamSupport.stream(all, false).collect(Collectors.toList());
	}

	public Optional<CaseDataRequest> findDetailed(UUID uuid) {
		var id = DataRequestIdentifier.of(uuid);
		return repository.findById(id);
	}

	public CaseDataRequest update(CaseDataRequest indexCase, IndexCaseUpdateDTO update) {
		if (StringUtils.isNotEmpty(update.getComment())) {
			indexCase.setComment(update.getComment());
		}
		if (StringUtils.isNotEmpty(update.getName())) {
			indexCase.setName(update.getName());
		}
		if (StringUtils.isNotEmpty(update.getExternalCaseId())) {
			indexCase.setRefId(update.getExternalCaseId());
		}
		if (update.getStatus() != null) {
			indexCase.setStatus(Status.valueOf(update.getStatus().name()));
		}
		return repository.save(indexCase);
	}

	public CaseDataRequest create(IndexCaseInsertDTO insert) {
		var dataRequest = CaseDataRequest
				.builder()
				.features(Set.of())
				.comment(insert.getComment())
				.refId(insert.getExternalCaseId())
				.name(insert.getName())
				.requestStart(insert.getStart())
				.requestEnd(insert.getEnd())
				.build();
		return repository.save(dataRequest);
	}

	public int getCountSinceDate(Instant date) {
		return repository.getCountSinceDate(date);
	}

	public int getCountWithStatus(Status status) {
		return repository.getCountWithStatus(status);
	}
}
