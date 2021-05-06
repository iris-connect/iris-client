package iris.client_bff.search_client.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SearchRequestDto {

    private String searchKeyword;
}
