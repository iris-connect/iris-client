package iris.client_bff.cases.web;

import static lombok.AccessLevel.*;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class IndexCaseMapper {

  public static IndexCaseDetailsDTO mapDetailed(CaseDataRequest indexCase) {
	return IndexCaseDetailsDTO.builder()
		.caseId(indexCase.getId().toString())
		.comment(indexCase.getComment())
		.externalCaseId(indexCase.getRefId())
		.name(indexCase.getName())
		.status(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()))
		.start(indexCase.getRequestStart())
		.end(indexCase.getRequestEnd())

		// TODO assign real data submission values
		.submissionData(null)
		.submissionUri(indexCase.getDwSubmissionUri())
		.build();
  }

  public static IndexCaseDTO map(CaseDataRequest indexCase) {

	return IndexCaseDTO.builder()
		.caseId(indexCase.getId().toString())
		.comment(indexCase.getComment())
		.externalCaseId(indexCase.getRefId())
		.name(indexCase.getName())
		.status(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()))
		.start(indexCase.getRequestStart())
		.end(indexCase.getRequestEnd())
		.build();
  }
}
