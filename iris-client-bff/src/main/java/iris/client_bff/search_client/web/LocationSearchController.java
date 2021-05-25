package iris.client_bff.search_client.web;

import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.search_client.web.dto.LocationList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

@RestController
@Validated
@RequiredArgsConstructor
public class LocationSearchController {

	private final SearchClient searchClient;

	@GetMapping("/search/{search_keyword}")
	@ResponseStatus(HttpStatus.OK)
	public LocationList searchSearchKeywordGet(
			@Size(min = 4) @PathVariable("search_keyword") String searchKeyword, Pageable pageable) {

		try {
			return searchClient.search(searchKeyword, pageable);
		} catch (IRISSearchException e) {
			// TODO handle
			throw new RuntimeException(e);
		}
	}
}
