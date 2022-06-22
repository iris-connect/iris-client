package iris.client_bff.cases;

import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.model.CaseDataSubmission.DataSubmissionIdentifier;
import iris.client_bff.core.model.AggregateRepository;

import javax.transaction.Transactional;

import org.springframework.data.util.Streamable;

public interface CaseDataSubmissionRepository
		extends AggregateRepository<CaseDataSubmission, DataSubmissionIdentifier> {

	@Transactional
	Streamable<CaseDataSubmission> findAllByRequest(CaseDataRequest request);

	@Transactional
	void deleteAllByRequestIn(Iterable<? extends CaseDataRequest> requests);
}
