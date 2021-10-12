package iris.client_bff.cases;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
public interface CaseDataRequestRepository extends JpaRepository<CaseDataRequest, DataRequestIdentifier> {

	@Query("select count(r) from CaseDataRequest r where r.metadata.created >= :date")
	int getCountSinceDate(Instant date);

	@Query("select count(r) from CaseDataRequest r where r.status = :status")
	int getCountWithStatus(Status status);

	Page<CaseDataRequest> findByStatus(Status status, Pageable pageable);

	Page<CaseDataRequest> findByRefIdContainsOrNameContainsAllIgnoreCase(String search, String search1,
			Pageable pageable);

	@Query("select r from CaseDataRequest r where r.status = :status and ( upper(r.refId) like concat('%', upper(:search), '%') or upper(r.name) like concat('%', upper(:search), '%'))")
	Page<CaseDataRequest> findByStatusAndSearchByRefIdOrName(Status status, String search, Pageable pageable);

	/**
	 * Returns the {@link CaseDataRequest}s created before the given {@link Instant}.
	 * 
	 * @param refDate must not be {@literal null}.
	 * @return
	 */
	Streamable<CaseDataRequest> findByMetadataCreatedIsBefore(Instant refDate);
}
