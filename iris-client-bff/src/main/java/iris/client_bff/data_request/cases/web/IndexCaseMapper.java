package iris.client_bff.data_request.cases.web;

import iris.client_bff.data_request.cases.CaseDataRequest;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDetailsDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseStatusDTO;

public class IndexCaseMapper {
  public static IndexCaseDetailsDTO mapDetailed(CaseDataRequest indexCase) {
    var indexCaseDetailsDTO = new IndexCaseDetailsDTO();
    indexCaseDetailsDTO.setCaseId(indexCase.getId().toString());
    indexCaseDetailsDTO.setComment(indexCase.getComment());
    indexCaseDetailsDTO.setExternalCaseId(indexCase.getRefId());
    indexCaseDetailsDTO.setName(indexCase.getName());
    indexCaseDetailsDTO.setStatus(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()));
    indexCaseDetailsDTO.setStart(indexCase.getRequestStart());
    indexCaseDetailsDTO.setEnd(indexCase.getRequestEnd());

    // TODO assign real data submission values
    indexCaseDetailsDTO.setSubmissionData(null);

    return indexCaseDetailsDTO;
  }

  public static IndexCaseDTO map(CaseDataRequest indexCase) {
    var indexCaseDTO = new IndexCaseDTO();
    indexCaseDTO.setComment(indexCase.getComment());
    indexCaseDTO.setCaseId(indexCase.getId().toString());
    indexCaseDTO.setExternalCaseId(indexCase.getRefId());
    indexCaseDTO.setName(indexCase.getName());
    indexCaseDTO.setStatus(IndexCaseStatusDTO.valueOf(indexCase.getStatus().name()));
    indexCaseDTO.setStart(indexCase.getRequestStart());
    indexCaseDTO.setEnd(indexCase.getRequestEnd());
    return indexCaseDTO;
  }
}
