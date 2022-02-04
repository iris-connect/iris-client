package iris.client_bff.core;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Jens Kutzsche
 */
@Getter
@Embeddable
public class Metadata {

	@CreatedDate
	@Column(updatable = false)
	@GenericField(sortable = Sortable.YES)
	Instant created;

	@LastModifiedDate
	@GenericField(sortable = Sortable.YES)
	Instant lastModified;

	@CreatedBy
	@Column(updatable = false)
	UUID createdBy;

	@LastModifiedBy
	UUID lastModifiedBy;
}
