package iris.client_bff.search_client.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.web.dto.LocationQueryResult;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Size;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class LocationSearchController {

	private final SearchClient searchClient;

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public LocationQueryResult searchSearchKeywordGet(@Size(min = 4) @RequestParam String search, Pageable pageable) {
		if (isSearchInputValid(search)) {
			return searchClient.search(search, pageable);
		} else {
			log.warn(ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE + " - search: " + search);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT_EXCEPTION_MESSAGE);
		}
	}

	private boolean isSearchInputValid(String search) {
		if (search == null || search.length() < 4 || ValidationHelper.checkInputForAttacks(search)) {
			return false;
		}

		return true;
	}
}
