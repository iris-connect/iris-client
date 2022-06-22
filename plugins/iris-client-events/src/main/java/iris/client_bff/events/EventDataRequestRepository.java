package iris.client_bff.events;

import iris.client_bff.core.model.AggregateRepository;
import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import iris.client_bff.events.EventDataRequest.Status;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Jens Kutzsche
 */
public interface EventDataRequestRepository extends AggregateRepository<EventDataRequest, DataRequestIdentifier> {

	@Query("select count(r) from EventDataRequest r where r.metadata.created >= :date")
	int getCountSinceDate(Instant date);

	@Query("select count(r) from EventDataRequest r where r.status = :status")
	int getCountWithStatus(Status status);

	Page<EventDataRequest> findByStatus(Status status, Pageable pageable);
}
