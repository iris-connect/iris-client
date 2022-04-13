package iris.client_bff.cases.web;

import static iris.client_bff.cases.web.IndexCaseMapper.*;
import static org.springframework.http.HttpStatus.*;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.CaseDataRequestService;
import iris.client_bff.cases.CaseDataSubmissionService;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import iris.client_bff.core.messages.ErrorMessages;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.users.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

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

@RestController
@RequestMapping("/data-requests-client/cases")
@RequiredArgsConstructor
public class CaseDataRequestController {

	private final CaseDataRequestService caseDataRequestService;

	private final CaseDataSubmissionService submissionService;
	private final UserDetailsServiceImpl userService;

	@GetMapping
	@ResponseStatus(OK)
	public Page<IndexCaseDTO> getAll(
			@RequestParam(required = false) Status status,
			@RequestParam(required = false) String search,
			Pageable pageable) {

		if (status != null && StringUtils.isNotEmpty(search)) {
			return caseDataRequestService.search(status, search, pageable).map(IndexCaseMapper::map);
		}
		if (StringUtils.isNotEmpty(search)) {
			return caseDataRequestService.search(search, pageable).map(IndexCaseMapper::map);
		}
		if (status != null) {
			return caseDataRequestService.findByStatus(status, pageable).map(IndexCaseMapper::map);
		}

		return caseDataRequestService.findAll(pageable).map(IndexCaseMapper::map);
	}

	@PostMapping
	@ResponseStatus(OK)
	public IndexCaseDetailsDTO create(@RequestBody @Valid IndexCaseInsertDTO insert) {

		try {
			return mapDetailed(caseDataRequestService.create(insert), userService);
		} catch (IRISDataRequestException e) {
			// TODO: use ExceptionMapper?
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.CASE_DATA_REQUEST_CREATION);
		}
	}

	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> getDetails(@PathVariable DataRequestIdentifier id) {
		return caseDataRequestService.findDetailed(id).map((dataRequest -> {
			var indexCaseDetailsDTO = mapDetailed(dataRequest, userService);

			submissionService.findByRequest(dataRequest).ifPresent(indexCaseDetailsDTO::setSubmissionData);

			return indexCaseDetailsDTO;
		})).map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> update(@PathVariable DataRequestIdentifier id,
			@RequestBody @Valid IndexCaseUpdateDTO update) {

		return caseDataRequestService.findDetailed(id)
				.map(it -> caseDataRequestService.update(it, update))
				.map(it -> mapDetailed(it, userService))
				.map(ResponseEntity::ok)
				.orElseGet(ResponseEntity.notFound()::build);
	}
}
