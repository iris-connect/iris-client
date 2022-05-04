package iris.client_bff.cases;

import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.model.CaseDataSubmission.DataSubmissionIdentifier;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

public interface CaseDataSubmissionRepository extends JpaRepository<CaseDataSubmission, DataSubmissionIdentifier> {

	@Transactional
	Streamable<CaseDataSubmission> findAllByRequest(CaseDataRequest request);

	@Transactional
	void deleteAllByRequestIn(Iterable<? extends CaseDataRequest> requests);

	@Query("select count(s)>0 from CaseDataSubmission s where s.metadata.createdBy = :userId OR s.metadata.lastModifiedBy = :userId")
	boolean isReferencedToUser(UUID userId);
}
