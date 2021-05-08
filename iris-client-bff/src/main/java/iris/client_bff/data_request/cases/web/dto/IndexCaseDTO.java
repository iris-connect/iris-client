package iris.client_bff.data_request.cases.web.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexCaseDTO {
	private String caseId;
	private String comment;
	private String externalCaseId;
	private String name;
	private Instant start;
	private Instant end;
	private IndexCaseStatusDTO status;
}
