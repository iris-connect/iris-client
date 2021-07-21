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
		IndexCaseInsertDTO insertValidated = indexCaseInsertDTOInputValidated(insert);

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
		if (ValidationHelper.isUUIDInputValid(id.toString())) {
			return caseDataRequestService.findDetailed(id).map((dataRequest -> {
				var indexCaseDetailsDTO = mapDetailed(dataRequest);

				submissionService.findByRequest(dataRequest).ifPresent(indexCaseDetailsDTO::setSubmissionData);

				return indexCaseDetailsDTO;
			})).map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - id: " + id.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> update(@PathVariable UUID id, @RequestBody @Valid IndexCaseUpdateDTO update) {
		if (ValidationHelper.isUUIDInputValid(id.toString())) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - id: " + id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		IndexCaseUpdateDTO updateValidated = indexCaseUpdateDTOInputValidated(update);

		ResponseEntity<IndexCaseDetailsDTO> responseEntity = caseDataRequestService.findDetailed(id)
			.map(it -> caseDataRequestService.update(it, updateValidated))
			.map(IndexCaseMapper::mapDetailed)
			.map(ResponseEntity::ok)
			.orElseGet(ResponseEntity.notFound()::build);

		caseDataRequestService.sendDataRecievedEmail(responseEntity.getBody(), updateValidated.getStatus());

		return responseEntity;
	}

	private IndexCaseUpdateDTO indexCaseUpdateDTOInputValidated(IndexCaseUpdateDTO update) {

		boolean isInvalid = false;

		if (update == null) {
			isInvalid = true;
		}

		if (update.getComment() != null) {
			if (!ValidationHelper.checkInputForAttacks(update.getComment())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - comment: " + update.getComment());
				update.setComment(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (update.getName() != null) {
			if (!ValidationHelper.checkInputForAttacks(update.getName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - name: " + update.getName());
				update.setName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (update.getExternalCaseId() != null) {
			if (!ValidationHelper.checkInputForAttacks(update.getExternalCaseId())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - externalCaseId: " + update.getExternalCaseId());
				update.setExternalCaseId(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (!(update.getStatus() == IndexCaseStatusDTO.DATA_RECEIVED
			|| update.getStatus() == IndexCaseStatusDTO.DATA_REQUESTED
			|| update.getStatus() == IndexCaseStatusDTO.ABORTED
			|| update.getStatus() == IndexCaseStatusDTO.CLOSED)) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - status: " + update.getStatus());
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return update;
	}

	private IndexCaseInsertDTO indexCaseInsertDTOInputValidated(IndexCaseInsertDTO insert) {

		boolean isInvalid = false;

		if (insert == null) {
			isInvalid = true;
		}

		if (insert.getComment() != null) {
			if (!ValidationHelper.checkInputForAttacks(insert.getComment())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - comment: " + insert.getComment());
				insert.setComment(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (insert.getName() != null) {
			if (!ValidationHelper.checkInputForAttacks(insert.getName())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - name: " + insert.getName());
				insert.setName(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (insert.getExternalCaseId() != null) {
			if (!ValidationHelper.checkInputForAttacks(insert.getExternalCaseId())) {
				log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - externalCaseId: " + insert.getExternalCaseId());
				insert.setExternalCaseId(ErrorMessages.INVALID_INPUT_STRING);
			}
		}

		if (insert.getStart() == null) {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - start: null");
			isInvalid = true;
		}

		if (isInvalid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}

		return insert;
	}
}
