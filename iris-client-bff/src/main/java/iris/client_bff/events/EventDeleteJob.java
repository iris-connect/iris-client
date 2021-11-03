package iris.client_bff.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class collects all old events and deletes this.
 *
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
class EventDeleteJob {

	private final @NonNull EventDataRequestRepository eventRequests;
	private final @NonNull EventDataSubmissionRepository eventSubmissions;
	private final @NonNull EventDeletionProperties properties;

	@Transactional
	@Scheduled(cron = "${iris.client.event.delete-cron:-}")
	void deleteEventRequests() {

		var refDate = LocalDate.now().minus(properties.getDeleteAfter()).atStartOfDay().atZone(ZoneId.systemDefault())
				.toInstant();

		var oldRequests = eventRequests.findByMetadataCreatedIsBefore(refDate).toList();

		if (oldRequests.isEmpty()) {
			return;
		}

		log.debug("{} event data request(s) are deleted with period {} after their creation!",
				oldRequests.size(),
				properties.getDeleteAfter(),
				oldRequests.get(0).getCreatedAt());

		eventSubmissions.deleteAllByRequestIn(oldRequests);
		eventRequests.deleteAll(oldRequests);

		log.info("{} event data request(s) (IDs: {}) were deleted with period {} after their creation at {}!",
				oldRequests.size(),
				oldRequests.stream().map(EventDataRequest::getId)
						.map(EventDataRequest.DataRequestIdentifier::toString)
						.collect(Collectors.joining(", ")),
				properties.getDeleteAfter(),
				oldRequests.get(0).getCreatedAt());
	}

	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("iris.client.event")
	@Getter
	public static class EventDeletionProperties {

		/**
		 * Defines the {@link Period} after that a event will be deleted starting from the creation date.
		 */
		private final Period deleteAfter;
	}
}
