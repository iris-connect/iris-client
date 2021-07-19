package iris.client_bff.cases.web;

import static iris.client_bff.cases.web.IndexCaseMapper.mapDetailed;
import static org.springframework.http.HttpStatus.OK;

import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.CaseDataRequestService;
import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import iris.client_bff.core.security.InputValidationUtility;
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

	private final InputValidationUtility inputValidationUtility;

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
		if (isIndexCaseInsertDTOInputValid(insert)) {
			try {
				return mapDetailed(caseDataRequestService.create(insert));
			} catch (IRISDataRequestException e) {
				// TODO: use ExceptionMapper?
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.CASE_DATA_REQUEST_CREATION);
			}
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + ": " + insert);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> getDetails(@PathVariable UUID id) {
		if (inputValidationUtility.isUUIDInputValid(id.toString())) {
			return caseDataRequestService.findDetailed(id).map((dataRequest -> {
				var indexCaseDetailsDTO = mapDetailed(dataRequest);

				submissionService.findByRequest(dataRequest).ifPresent(indexCaseDetailsDTO::setSubmissionData);

				return indexCaseDetailsDTO;
			})).map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + ": " + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> update(@PathVariable UUID id, @RequestBody @Valid IndexCaseUpdateDTO update) {
		if (inputValidationUtility.isUUIDInputValid(id.toString()) || isIndexCaseUpdateDTOInputValid(update)) {
			ResponseEntity<IndexCaseDetailsDTO> responseEntity = caseDataRequestService.findDetailed(id)
				.map(it -> caseDataRequestService.update(it, update))
				.map(IndexCaseMapper::mapDetailed)
				.map(ResponseEntity::ok)
				.orElseGet(ResponseEntity.notFound()::build);

			caseDataRequestService.sendDataRecievedEmail(responseEntity.getBody(), update.getStatus());

			return responseEntity;
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + ": " + id + ", " + update);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	private boolean isIndexCaseUpdateDTOInputValid(IndexCaseUpdateDTO update) {
		if (update == null) {
			return false;
		}

		if (update.getComment() != null) {
			if (!inputValidationUtility.checkInputForAttacks(update.getComment())) {
				return false;
			}
		}

		if (update.getName() != null) {
			if (!inputValidationUtility.checkInputForAttacks(update.getName())) {
				return false;
			}
		}

		if (update.getExternalCaseId() != null) {
			if (!inputValidationUtility.checkInputForAttacks(update.getExternalCaseId())) {
				return false;
			}
		}

		return true;
	}

	private boolean isIndexCaseInsertDTOInputValid(IndexCaseInsertDTO insert) {
		if (insert == null || insert.getStart() == null) {
			return false;
		}

		if (insert.getComment() != null) {
			if (!inputValidationUtility.checkInputForAttacks(insert.getComment())) {
				return false;
			}
		}

		if (insert.getName() != null) {
			if (!inputValidationUtility.checkInputForAttacks(insert.getName())) {
				return false;
			}
		}

		if (insert.getExternalCaseId() != null) {
			if (!inputValidationUtility.checkInputForAttacks(insert.getExternalCaseId())) {
				return false;
			}
		}

		return true;
	}
}
