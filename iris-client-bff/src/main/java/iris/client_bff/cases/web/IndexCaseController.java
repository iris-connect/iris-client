package iris.client_bff.cases.web;

import iris.client_bff.cases.IndexCaseService;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static iris.client_bff.cases.web.IndexCaseMapper.mapDetailed;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/data-requests-client/cases")
public class IndexCaseController {

	IndexCaseService indexCaseService;

	// @GetMapping
	// @ResponseStatus(OK)
	// public List<IndexCaseDTO> getAll() {
	//
	// return indexCaseService.findAll().stream()
	// .map(IndexCaseMapper::map)
	// .collect(Collectors.toList());
	// }

	@GetMapping
	@ResponseStatus(OK)
	public List<IndexCaseDTO> getAll(Pageable pageable) {
		return indexCaseService.findAll(pageable).stream()
				.map(IndexCaseMapper::map)
				.collect(Collectors.toList());
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
	public ResponseEntity<IndexCaseDetailsDTO> update(@PathVariable UUID id,
			@RequestBody @Valid IndexCaseUpdateDTO update) {

		return indexCaseService.findDetailed(id)
				.map(it -> indexCaseService.update(it, update))
				.map(IndexCaseMapper::mapDetailed)
				.map(ResponseEntity::ok)
				.orElseGet(ResponseEntity.notFound()::build);
	}
}
