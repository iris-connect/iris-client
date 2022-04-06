package iris.client_bff.search_client.web;

import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class LocationSearchController {

	private final SearchClient searchClient;

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public LocationQueryResult searchSearchKeywordGet(
			@RequestParam @Size(min = 4, max = 100) @NoSignOfAttack(obfuscateLogging = false) String search,
			Pageable pageable) {

		return searchClient.search(search, pageable);
	}
}
