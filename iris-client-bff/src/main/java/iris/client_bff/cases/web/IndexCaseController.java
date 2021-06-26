package iris.client_bff.cases.web;

import static iris.client_bff.cases.web.IndexCaseMapper.mapDetailed;
import static org.springframework.http.HttpStatus.OK;

import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.IndexCaseService;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import lombok.AllArgsConstructor;

import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RestController
@AllArgsConstructor
@RequestMapping("/data-requests-client/cases")
public class IndexCaseController {

	IndexCaseService indexCaseService;

	@GetMapping
	@ResponseStatus(OK)
	public Page<IndexCaseDTO> getAll(
		@RequestParam(required = false) Status status,
		@RequestParam(required = false) String search,
		Pageable pageable) {
		if (status != null && StringUtils.isNotEmpty(search)) {
			return indexCaseService.findByStatusAndSearchByRefIdOrName(status, search, pageable).map(IndexCaseMapper::map);
		} else if (StringUtils.isNotEmpty(search)) {
			return indexCaseService.searchByRefIdOrName(search, pageable).map(IndexCaseMapper::map);
		} else if (status != null) {
			return indexCaseService.findByStatus(status, pageable).map(IndexCaseMapper::map);
		}
		return indexCaseService.findAll(pageable).map(IndexCaseMapper::map);
	}

	@PostMapping
	@ResponseStatus(OK)
	public IndexCaseDetailsDTO create(@RequestBody @Valid IndexCaseInsertDTO insert) {
		return mapDetailed(indexCaseService.create(insert));
	}

	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> getDetails(@PathVariable UUID id) {

		return indexCaseService.findDetailed(id)
			.map(IndexCaseMapper::mapDetailed)
			.map(ResponseEntity::ok)
			.orElseGet(ResponseEntity.notFound()::build);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(OK)
	public ResponseEntity<IndexCaseDetailsDTO> update(@PathVariable UUID id, @RequestBody @Valid IndexCaseUpdateDTO update) {
		ResponseEntity<IndexCaseDetailsDTO> responseEntity = indexCaseService.findDetailed(id)
			.map(it -> indexCaseService.update(it, update))
			.map(IndexCaseMapper::mapDetailed)
			.map(ResponseEntity::ok)
			.orElseGet(ResponseEntity.notFound()::build);

		indexCaseService.sendDataRecievedEmail(responseEntity.getBody(), update.getStatus());

		return responseEntity;
	}
}