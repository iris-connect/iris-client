package iris.client_bff.cases;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
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
 * This class collects all old cases and deletes this.
 *
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
class CaseDeleteJob {

	private final @NonNull CaseDataRequestRepository caseRequests;
	private final @NonNull CaseDataSubmissionRepository caseSubmissions;
	private final @NonNull CaseDeletionProperties properties;

	@Transactional
	@Scheduled(cron = "${iris.client.case.delete-cron:-}")
	void deleteCaseRequests() {

		var refDate = LocalDate.now().minus(properties.getDeleteAfter()).atStartOfDay().atZone(ZoneId.systemDefault())
				.toInstant();

		var oldRequests = caseRequests.findByMetadataCreatedIsBefore(refDate).toList();

		if (oldRequests.isEmpty()) {
			return;
		}

		log.debug("{} case data request(s) are deleted with period {} after their creation!",
				oldRequests,
				properties.getDeleteAfter(),
				oldRequests.get(0).getCreatedAt());

		caseSubmissions.deleteAllByRequestIn(oldRequests);
		caseRequests.deleteAll(oldRequests);

		log.info("{} case data request(s) (IDs: {}) were deleted with period {} after their creation at {}!",
				oldRequests.size(),
				oldRequests.stream().map(CaseDataRequest::getId)
						.map(DataRequestIdentifier::toString)
						.collect(Collectors.joining(", ")),
				properties.getDeleteAfter(),
				oldRequests.get(0).getCreatedAt());
	}

	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("iris.client.case")
	@Getter
	public static class CaseDeletionProperties {

		/**
		 * Defines the {@link Period} after that a case will be deleted starting from the creation date.
		 */
		private final Period deleteAfter;
	}
}
