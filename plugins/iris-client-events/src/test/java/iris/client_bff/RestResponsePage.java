package iris.client_bff;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class was added for testing purposes only and is required for the deserialization of Page JSONs
 *
 * @param <T>
 */
@JsonIgnoreProperties({ "pageable", "last", "first", "numberOfElements", "sort", "totalPages", "empty" })
public class RestResponsePage<T> extends PageImpl<T> {

	@JsonCreator
	public RestResponsePage(@JsonProperty("content") List<T> content,
			@JsonProperty("number") int number,
			@JsonProperty("size") int size,
			@JsonProperty("totalElements") Long totalElements) {
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
