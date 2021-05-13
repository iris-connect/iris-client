package iris.client_bff.statistics.web.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticsDTO {
	private int eventsCount;
	private int indexCasesCount;
	private int sumStatus;
}
