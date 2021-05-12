package iris.client_bff.data_request.statistics.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {
	private int eventsCount;
	private int indexCasesCount;
	private int sumStatus;
}
