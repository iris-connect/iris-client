package iris.client_bff.events;

import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.model.EventDataSubmission.DataSubmissionIdentifier;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
public interface EventDataSubmissionRepository extends JpaRepository<EventDataSubmission, DataSubmissionIdentifier> {

	@Transactional
	Streamable<EventDataSubmission> findAllByRequest(EventDataRequest request);

	@Transactional
	void deleteAllByRequestIn(Iterable<? extends EventDataRequest> requests);

	@Query("select count(s)>0 from EventDataSubmission s where s.metadata.createdBy = :userId OR s.metadata.lastModifiedBy = :userId")
	boolean isReferencedToUser(UUID userId);
}
