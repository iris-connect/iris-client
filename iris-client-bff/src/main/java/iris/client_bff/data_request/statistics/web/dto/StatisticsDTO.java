package iris.client_bff.data_request.statistics.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticsDTO {
	private int eventsCount;
	private int indexCasesCount;
	private int sumStatus;
}
