package iris.client_bff.search_client.web;

import iris.client_bff.core.web.error.ResponseStatusExceptionMapper;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

@RestController
@Validated
@RequiredArgsConstructor
public class LocationSearchController {

	private final ResponseStatusExceptionMapper exceptionMapper;
	private final SearchClient searchClient;

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public LocationQueryResult searchSearchKeywordGet(
			@Size(min = 4) @RequestParam String search, Pageable pageable) {

		try {
			return searchClient.search(search, pageable);
		} catch (IRISSearchException e) {
			throw exceptionMapper.mapError(e);
		}
	}
}
