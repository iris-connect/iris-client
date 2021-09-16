package iris.client_bff;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was added for testing purposes only and is required for the deserialization of Page JSONs
 * @param <T>
 */
public class RestResponsePage<T> extends PageImpl<T> {

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	// @JsonIgnoreProperties(ignoreUnknown = true)
	public RestResponsePage(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
			@JsonProperty("size") int size,
			@JsonProperty("totalElements") Long totalElements, @JsonProperty("pageable") JsonNode pageable,
			@JsonProperty("last") boolean last,
			@JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
			@JsonProperty("first") boolean first,
			@JsonProperty("numberOfElements") int numberOfElements) {
		super(content, PageRequest.of(number, size), totalElements);
	}

	public RestResponsePage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public RestResponsePage(List<T> content) {
		super(content);
	}

	public RestResponsePage() {
		super(new ArrayList<T>());
	}

}
