package iris.client_bff.cases.web;

import static iris.client_bff.cases.web.IndexCaseMapper.mapDetailed;
import static org.springframework.http.HttpStatus.OK;

import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.CaseDataRequestService;
import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/data-requests-client/cases")
public class CaseDataRequestController {

	private static final String EXCEPTION_MESSAGE_ID = " - id: ";
	private static final String EXCEPTION_MESSAGE_STATUS = " - status: ";
	private static final String EXCEPTION_MESSAGE_COMMENT = " - comment: ";
	private static final String EXCEPTION_MESSAGE_NAME = " - name: ";
	private static final String EXCEPTION_MESSAGE_EXTERNAL_CASE_ID = " - externalCaseId: ";

	private final CaseDataRequestService caseDataRequestService;

	private final CaseDataSubmissionService submissionService;

	@GetMapping
	@ResponseStatus(OK)
	public Page<IndexCaseDTO> getAll(
		@RequestParam(required = false) Status status,
		@RequestParam(required = false) String search,
		Pageable pageable) {
		if (status != null && StringUtils.isNotEmpty(search)) {
			return caseDataRequestService.findByStatusAndSearchByRefIdOrName(status, search, pageable).map(IndexCaseMapper::map);
		} else if (StringUtils.isNotEmpty(search)) {
			return caseDataRequestService.searchByRefIdOrName(search, pageable).map(IndexCaseMapper::map);
		} else if (status != null) {
			return caseDataRequestService.findByStatus(status, pageable).map(IndexCaseMapper::map);
		}
		return caseDataRequestService.findAll(pageable).map(IndexCaseMapper::map);
	}

	@PostMapping
	@ResponseStatus(OK)
	public IndexCaseDetailsDTO create(@RequestBody @Valid IndexCaseInsertDTO insert) {
		IndexCaseInsertDTO insertValidated = validateIndexCaseInsertDTO(insert);

		try {
			return mapDetailed(caseDataRequestService.create(insertValidated));
		} catch (IRISDataRequestException e) {
			// TODO: use ExceptionMapper?
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.CASE_DATA_REQUEST_CREATION);
		}
	}

	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> getDetails(@PathVariable UUID id) {
		if (ValidationHelper.isUUIDInputValid(id.toString(), EXCEPTION_MESSAGE_ID + id.toString())) {
			return caseDataRequestService.findDetailed(id).map((dataRequest -> {
				var indexCaseDetailsDTO = mapDetailed(dataRequest);

				submissionService.findByRequest(dataRequest).ifPresent(indexCaseDetailsDTO::setSubmissionData);

				return indexCaseDetailsDTO;
			})).map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> update(@PathVariable UUID id, @RequestBody @Valid IndexCaseUpdateDTO update) {
		if (!ValidationHelper.isUUIDInputValid(id.toString(), EXCEPTION_MESSAGE_ID + id.toString())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		IndexCaseUpdateDTO updateValidated = validateIndexCaseUpdateDTO(update);

		ResponseEntity<IndexCaseDetailsDTO> responseEntity = caseDataRequestService.findDetailed(id)
			.map(it -> caseDataRequestService.update(it, updateValidated))
			.map(IndexCaseMapper::mapDetailed)
			.map(ResponseEntity::ok)
			.orElseGet(ResponseEntity.notFound()::build);

		return responseEntity;
	}

	private IndexCaseUpdateDTO validateIndexCaseUpdateDTO(IndexCaseUpdateDTO update) {
		if (update == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (ValidationHelper.isPossibleAttack(update.getComment(), EXCEPTION_MESSAGE_COMMENT + update.getComment())) {
			update.setComment(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(update.getName(), EXCEPTION_MESSAGE_NAME + update.getName())) {
			update.setName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(update.getExternalCaseId(), EXCEPTION_MESSAGE_EXTERNAL_CASE_ID + update.getExternalCaseId())) {
			update.setExternalCaseId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (update.getStatus() != null
			&& !(update.getStatus() == IndexCaseStatusDTO.DATA_RECEIVED
				|| update.getStatus() == IndexCaseStatusDTO.DATA_REQUESTED
				|| update.getStatus() == IndexCaseStatusDTO.ABORTED
				|| update.getStatus() == IndexCaseStatusDTO.CLOSED)) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + EXCEPTION_MESSAGE_STATUS + update.getStatus());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return update;
	}

	private IndexCaseInsertDTO validateIndexCaseInsertDTO(IndexCaseInsertDTO insert) {
		if (insert == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		if (ValidationHelper.isPossibleAttack(insert.getComment(), EXCEPTION_MESSAGE_COMMENT + insert.getComment())) {
			insert.setComment(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(insert.getName(), EXCEPTION_MESSAGE_NAME + insert.getName())) {
			insert.setName(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (ValidationHelper.isPossibleAttack(insert.getExternalCaseId(), EXCEPTION_MESSAGE_EXTERNAL_CASE_ID + insert.getExternalCaseId())) {
			insert.setExternalCaseId(ErrorMessages.INVALID_INPUT_STRING);
		}

		if (insert.getStart() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - start: null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return insert;
	}
}
