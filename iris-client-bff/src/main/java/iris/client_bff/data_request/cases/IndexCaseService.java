package iris.client_bff.data_request.cases;

import iris.client_bff.data_request.DataRequest.DataRequestIdentifier;
import iris.client_bff.data_request.DataRequest.Status;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDetailsDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseInsertDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseStatusDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseUpdateDTO;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IndexCaseService {

	CaseDataRequestRepository repository;

	public List<IndexCaseDTO> findAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(this::map)
				.collect(Collectors.toList());
	}

	public IndexCaseDetailsDTO findDetailed(UUID id) {
		var dataRequest = findOrThrow(id);
		return mapDetailed(dataRequest);
	}

	public IndexCaseDetailsDTO update(UUID id, IndexCaseUpdateDTO update) {
		var indexCase = findOrThrow(id);
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
		return mapDetailed(repository.save(indexCase));
	}

	public IndexCaseDetailsDTO create(IndexCaseInsertDTO insert) {
		var dataRequest = CaseDataRequest
				.builder()
				.features(Set.of())
				.comment(insert.getComment())
				.refId(insert.getExternalCaseId())
				.name(insert.getName())
				.requestStart(insert.getStart())
				.requestEnd(insert.getEnd())
				.build();
		return mapDetailed(repository.save(dataRequest));
	}

	public IndexCaseDetailsDTO mapDetailed(CaseDataRequest indexCase) {
		var indexCaseDetailsDTO = new IndexCaseDetailsDTO();
		indexCaseDetailsDTO.setCaseId(indexCase.getId().toString());
		indexCaseDetailsDTO.setComment(indexCase.getComment());
		indexCaseDetailsDTO.setExternalCaseId(indexCase.getRefId());
		indexCaseDetailsDTO.setName(indexCase.getName());
		indexCaseDetailsDTO.setStatus(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()));
		indexCaseDetailsDTO.setStart(indexCase.getRequestStart());
		indexCaseDetailsDTO.setEnd(indexCase.getRequestEnd());

		// TODO assign real data submission values
		indexCaseDetailsDTO.setSubmissionData(null);

		return indexCaseDetailsDTO;
	}

	public IndexCaseDTO map(CaseDataRequest indexCase) {
		var indexCaseDTO = new IndexCaseDTO();
		indexCaseDTO.setComment(indexCase.getComment());
		indexCaseDTO.setCaseId(indexCase.getId().toString());
		indexCaseDTO.setExternalCaseId(indexCase.getRefId());
		indexCaseDTO.setName(indexCase.getName());
		indexCaseDTO.setStatus(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()));
		indexCaseDTO.setStart(indexCase.getRequestStart());
		indexCaseDTO.setEnd(indexCase.getRequestEnd());
		return indexCaseDTO;
	}

	private CaseDataRequest findOrThrow(UUID uuid) {
		var id = DataRequestIdentifier.of(uuid);
		var optional = repository.findById(id);
		if (optional.isEmpty()) {
			var error = "IndexCaseDataRequest not found: " + id.toString();
			log.error(error);
			throw new RuntimeException(error);
		}
		return optional.get();
	}

}
