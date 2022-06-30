package iris.client_bff.feedback;

import lombok.Getter;
import lombok.NonNull;

/**
 * This class represents the json structure of a feedback response.
 *
 * @author Ostfalia Gruppe 12
 * @author Jens Kutzsche
 */
@Getter
public class FeedbackResponseDto {

	private @NonNull String issueNumber;
}
