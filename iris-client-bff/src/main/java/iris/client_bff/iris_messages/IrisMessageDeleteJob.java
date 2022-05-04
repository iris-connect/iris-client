package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.IrisMessage.IrisMessageIdentifier;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
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
class IrisMessageDeleteJob {

	private final @NonNull IrisMessageRepository messageRepo;
	private final @NonNull Properties properties;

	@Transactional
	@Scheduled(cron = "${iris.client.message.delete-cron:-}")
	void deleteMessages() {

		var refDate = Instant.now().minus(properties.getDeleteAfter());

		var oldMessages = messageRepo.findByMetadataCreatedIsBefore(refDate).toList();

		if (oldMessages.isEmpty()) {
			return;
		}

		log.debug("{} IRIS message(s) are deleted with period {} after their creation!",
				oldMessages.size(),
				properties.getDeleteAfter(),
				oldMessages.get(0).getCreatedAt());

		messageRepo.deleteAll(oldMessages);

		log.info("{} IRIS message(s) (IDs: {}) were deleted with period {} after their creation at {}!",
				oldMessages.size(),
				oldMessages.stream()
						.map(IrisMessage::getId)
						.map(IrisMessageIdentifier::toString)
						.collect(Collectors.joining(", ")),
				properties.getDeleteAfter(),
				oldMessages.get(0).getCreatedAt());
	}

	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("iris.client.message")
	@Getter
	public static class Properties {

		/**
		 * Defines the {@link Duration} after that a IRIS message will be deleted starting from the creation date.
		 */
		private final Duration deleteAfter;
	}
}
