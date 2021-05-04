package iris.client_bff.search_client.web;

import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.web.dto.LocationList;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class LocationSearchController {

	private final SearchClient searchClient;

	@GetMapping("/search/{search_keyword}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<LocationList> searchSearchKeywordGet(
			@Size(min = 4) @PathVariable("search_keyword") String searchKeyword) {

		return new ResponseEntity<>(searchClient.search(searchKeyword), HttpStatus.OK);
	}
}
