package iris.client_bff.data_request.cases.web.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class IndexCaseDTO {
	private String caseId;
	private String comment;
	private String externalCaseId;
	private String name;
	private String nonce; // TODO check if required here
	private Instant start;
	private Instant end;
	private IndexCaseStatusDTO status;
}
