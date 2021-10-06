package iris.client_bff.events;

import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest.Status;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
public interface EventDataRequestRepository extends JpaRepository<EventDataRequest, DataRequestIdentifier> {

	@Query("select count(1) = 0 from EventDataRequest r where r.id = :code")
	boolean isCodeAvailable(UUID code);

	@Query("select count(r) from EventDataRequest r where r.metadata.created >= :date")
	int getCountSinceDate(Instant date);

	@Query("select count(r) from EventDataRequest r where r.status = :status")
	int getCountWithStatus(Status status);

	Page<EventDataRequest> findByStatus(Status status, Pageable pageable);

	Page<EventDataRequest> findByRefIdContainsOrNameContainsAllIgnoreCase(String search, String search1,
			Pageable pageable);

	@Query("select r from EventDataRequest r where r.status = :status and ( upper(r.refId) like concat('%', upper(:search), '%') or upper(r.name) like concat('%', upper(:search), '%'))")
	Page<EventDataRequest> findByStatusAndSearchByRefIdOrName(Status status, String search, Pageable pageable);

	/**
	 * Returns the {@link EventDataRequest}s created before the given {@link Instant}.
	 * 
	 * @param refDate must not be {@literal null}.
	 * @return
	 */
	Streamable<EventDataRequest> findByMetadataCreatedIsBefore(Instant refDate);
}
