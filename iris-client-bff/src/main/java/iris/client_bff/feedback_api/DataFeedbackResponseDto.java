package iris.client_bff.feedback_api;

import lombok.NonNull;
import lombok.ToString;

/*
 * This class represents the json structure of a response of Iris-Public-Server.
 * The incoming json will be mapped on this class after a post request for feedback data has been sent.
 * @author Ostfalia Gruppe 12
 */
@ToString
public class DataFeedbackResponseDto extends DataFeedbackRequestDto {

	private @NonNull String gitIssueId;

	public String getGitIssueId() {
		return gitIssueId;
	}

	public void setGitIssueId(String gitIssueId) {
		this.gitIssueId = gitIssueId;
	}

}
