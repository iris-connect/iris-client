package iris.client_bff.cases.web.request_dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class IndexCaseUpdateDTO {
	private String comment;
	private String externalCaseId;
	private String name;
	private IndexCaseStatusDTO status;
}
