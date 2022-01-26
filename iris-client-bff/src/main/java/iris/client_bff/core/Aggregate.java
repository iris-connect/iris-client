package iris.client_bff.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

/**
 * Base class of aggregates in the sense of DDD
 * 
 * @author Jens Kutzsche
 */
@MappedSuperclass
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public abstract class Aggregate<T extends Aggregate<T, ID>, ID extends Id> extends AbstractAggregateRoot<T>
		implements Persistable<ID> {

	@NonNull
	protected @EmbeddedId ID id;

	@IndexedEmbedded
	private Metadata metadata = new Metadata();
	private @Transient boolean isNew = true;

	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}

	public Instant getLastModifiedAt() {
		return this.getMetadata().getLastModified();
	}

	public String getLastModifiedBy() {
		return getMetadata().getLastModifiedBy();
	}

	public Instant getCreatedAt() {
		return this.getMetadata().getCreated();
	}

	public String getCreatedBy() {
		return getMetadata().getCreatedBy();
	}
}
