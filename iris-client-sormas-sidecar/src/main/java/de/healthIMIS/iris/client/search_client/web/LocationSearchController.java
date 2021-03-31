package de.healthIMIS.iris.client.search_client.web;

import de.healthIMIS.iris.api.sidecarclient.model.LocationList;
import de.healthIMIS.iris.client.search_client.SearchClient;
import javax.validation.constraints.DecimalMin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationSearchController {

  private SearchClient searchClient;

  public LocationSearchController(SearchClient searchClient) {
    this.searchClient = searchClient;
  }

  @GetMapping("/search/{search_keyword}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<LocationList> searchSearchKeywordGet(
      @DecimalMin("4") @PathVariable("search_keyword") String searchKeyword) {
    // TODO: Authenticate API Access
    return new ResponseEntity<LocationList>(searchClient.search(searchKeyword), HttpStatus.OK);
  }

}
