package iris.client_bff.search_client.eps.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordSearch {

	private String searchKeyword;
	private PageableDto pageable;
}
