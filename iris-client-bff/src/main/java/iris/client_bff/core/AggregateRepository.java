package iris.client_bff.core;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
@NoRepositoryBean
public interface AggregateRepository<T extends Aggregate<T, ID>, ID extends Id> extends JpaRepository<T, ID> {

	/**
	 * Returns the {@link Aggregate}s of type {@link T} created before the given {@link Instant}.
	 *
	 * @param refDate must not be {@literal null}.
	 * @return
	 */
	Streamable<T> findByMetadataCreatedIsBefore(Instant refDate);

	boolean existsByMetadataCreatedByIsOrMetadataLastModifiedByIs(UUID userId, UUID userId2);

	default boolean isReferencedToUser(UUID userId) {
		return existsByMetadataCreatedByIsOrMetadataLastModifiedByIs(userId, userId);
	}
}
