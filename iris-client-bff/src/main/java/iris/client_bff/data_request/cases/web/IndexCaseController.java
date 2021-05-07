package iris.client_bff.data_request.cases.web;

import static iris.client_bff.data_request.cases.web.IndexCaseMapper.mapDetailed;
import static org.springframework.http.HttpStatus.OK;

import iris.client_bff.data_request.cases.CaseDataRequest;
import iris.client_bff.data_request.cases.IndexCaseService;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDetailsDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseInsertDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseUpdateDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    return indexCaseService.findAll().stream().map(IndexCaseMapper::map)
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

    var optional = indexCaseService.findDetailed(id);

    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapDetailed(optional.get()));
  }

  @PatchMapping("/{id}")
  @ResponseStatus(OK)
  public ResponseEntity<IndexCaseDetailsDTO> update(@PathVariable UUID id,
      @RequestBody @Valid IndexCaseUpdateDTO update) {

    var optional = indexCaseService.findDetailed(id);

    if (optional.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var dataRequest = optional.get();
    var updated = indexCaseService.update(dataRequest, update);
    return ResponseEntity.ok(mapDetailed(updated));
  }
}
