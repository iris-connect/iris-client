package iris.client_bff.events.web;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocation;
import iris.client_bff.events.web.dto.ExistingDataRequestClientWithLocation.StatusEnum;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class EventMapper {
	public static ExistingDataRequestClientWithLocation map(EventDataRequest eventDataRequest) {
		return ExistingDataRequestClientWithLocation.builder()
				.name(eventDataRequest.getName())
				.code(eventDataRequest.getId().toString())
				.start(eventDataRequest.getRequestStart())
				.end(eventDataRequest.getRequestEnd())
				.status(StatusEnum.valueOf(eventDataRequest.getStatus().toString()))
				.lastUpdatedAt(eventDataRequest.getLastModifiedAt())
				.requestedAt(eventDataRequest.getCreatedAt())
				.externalRequestId(eventDataRequest.getRefId())
				.requestDetails(eventDataRequest.getRequestDetails())
				.build();
	}
}
