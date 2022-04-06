package iris.client_bff.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

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
		return getMetadata(Metadata::getLastModified);
	}

	public UUID getLastModifiedBy() {
		return getMetadata(Metadata::getLastModifiedBy);
	}

	public Instant getCreatedAt() {
		return getMetadata(Metadata::getCreated);
	}

	public UUID getCreatedBy() {
		return getMetadata(Metadata::getCreatedBy);
	}

	private <R> R getMetadata(Function<Metadata, R> getter) {

		// The null check must be because Hibernate does not create an object if all fields are null.
		// https://stackoverflow.com/questions/13606220/embedded-object-not-instantiated-automatically-if-it-has-no-basic-datatype-fiel
		return getMetadata() == null ? null : getter.apply(getMetadata());
	}
}
