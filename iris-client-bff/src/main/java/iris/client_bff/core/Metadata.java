package iris.client_bff.core;

import lombok.Getter;

import java.time.Instant;

import javax.persistence.Embeddable;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Jens Kutzsche
 */
@Getter
@Embeddable
public class Metadata {

	@CreatedDate
	@GenericField(sortable = Sortable.YES)
	Instant created;
	@LastModifiedDate
	@GenericField(sortable = Sortable.YES)
	Instant lastModified;
}
