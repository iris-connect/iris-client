package iris.client_bff.cases;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.core.model.AggregateRepository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Jens Kutzsche
 */
public interface CaseDataRequestRepository extends AggregateRepository<CaseDataRequest, DataRequestIdentifier> {

	@Query("select count(r) from CaseDataRequest r where r.metadata.created >= :date")
	int getCountSinceDate(Instant date);

	@Query("select count(r) from CaseDataRequest r where r.status = :status")
	int getCountWithStatus(Status status);

	Page<CaseDataRequest> findByStatus(Status status, Pageable pageable);

	Optional<CaseDataRequest> findByIdOrDataAuthorizationToken(DataRequestIdentifier id, String dataAuthorizationToken);
}
