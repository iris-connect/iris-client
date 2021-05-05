package iris.client_bff.data_request.cases.web;

import static org.springframework.http.HttpStatus.OK;

import iris.client_bff.data_request.cases.IndexCaseService;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDetailsDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseInsertDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseUpdateDTO;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/data-requests-client/cases")
public class IndexCaseController {

	IndexCaseService indexCaseService;

	@GetMapping
	@ResponseStatus(OK)
	public List<IndexCaseDTO> getAll() {
		return indexCaseService.findAll();
	}

	@PostMapping
	@ResponseStatus(OK)
	public IndexCaseDetailsDTO create(@RequestBody @Valid IndexCaseInsertDTO insert) {
		return indexCaseService.create(insert);
	}

	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public IndexCaseDetailsDTO getDetails(@PathVariable UUID id) {
		return indexCaseService.findDetailed(id);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(OK)
	public IndexCaseDetailsDTO update(@PathVariable UUID id,
			@RequestBody @Valid IndexCaseUpdateDTO update) {
		return indexCaseService.update(id, update);
	}
}
