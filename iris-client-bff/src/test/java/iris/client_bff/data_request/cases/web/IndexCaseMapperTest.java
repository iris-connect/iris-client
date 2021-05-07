package iris.client_bff.data_request.cases.web;

import static org.junit.jupiter.api.Assertions.*;

import iris.client_bff.data_request.DataRequest.Status;
import iris.client_bff.data_request.cases.CaseDataRequest;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseDetailsDTO;
import iris.client_bff.data_request.cases.web.dto.IndexCaseStatusDTO;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class IndexCaseMapperTest {

  Instant START = Instant.now();
  Instant END = Instant.now();

  @Test
  void mapDetailed() {
    var request = getRequest();
    var mapped = IndexCaseMapper.mapDetailed(request);

    var expected = IndexCaseDetailsDTO.builder()
        .name("request_name")
        .status(IndexCaseStatusDTO.ABORTED)
        .externalCaseId("my-ref-id")
        .start(START)
        .end(END)
        .comment("a-comment-here")
        .caseId(request.getId().toString())
        .build();

    assertEquals(expected, mapped);
  }

  @Test
  void map() {
    var request = getRequest();
    var mapped = IndexCaseMapper.map(request);

    var expected = IndexCaseDTO.builder()
        .name("request_name")
        .status(IndexCaseStatusDTO.ABORTED)
        .externalCaseId("my-ref-id")
        .start(START)
        .end(END)
        .comment("a-comment-here")
        .caseId(request.getId().toString())
        .build();

    assertEquals(expected, mapped);
  }

  private CaseDataRequest getRequest() {
    CaseDataRequest dataRequest = new CaseDataRequest();
    dataRequest.setName("request_name");
    dataRequest.setStatus(Status.ABORTED);
    dataRequest.setRefId("my-ref-id");
    dataRequest.setRequestStart(START);
    dataRequest.setRequestEnd(END);
    dataRequest.setComment("a-comment-here");
    dataRequest.setHdUserId("a-hd-user-id");
    return dataRequest;
  }
}