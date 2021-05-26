package iris.client_bff.search_client.eps.dto;

import lombok.*;
import org.springframework.data.domain.Sort;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableDto {
	private int page;
	private int size;
	private String sortBy;
	private Sort.Direction direction = Sort.DEFAULT_DIRECTION;
}