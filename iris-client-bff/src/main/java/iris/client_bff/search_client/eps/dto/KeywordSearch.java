package iris.client_bff.search_client.eps.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class KeywordSearch {

	private String searchKeyword;
}
