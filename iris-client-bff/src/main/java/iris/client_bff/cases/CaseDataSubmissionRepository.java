package iris.client_bff.cases;

import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.events.model.EventDataSubmission.DataSubmissionIdentifier;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface CaseDataSubmissionRepository extends CrudRepository<CaseDataSubmission, DataSubmissionIdentifier> {

	@Transactional
	Streamable<CaseDataSubmission> findAllByRequest(CaseDataRequest request);
}
