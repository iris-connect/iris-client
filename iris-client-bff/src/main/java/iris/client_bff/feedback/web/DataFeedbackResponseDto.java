package iris.client_bff.feedback.web;

import lombok.NonNull;

/*
 * This class represents the json structure of a response of EPS.
 * The input json will be mapped on this class after a post request for feedback data has been sent.
 * @author Ostfalia Gruppe 12
 */
public class DataFeedbackResponseDto {

	private @NonNull String gitIssueId;

	public String getGitIssueId() {
		return gitIssueId;
	}

	public void setGitIssueId(String gitIssueId) {
		this.gitIssueId = gitIssueId;
	}

}
