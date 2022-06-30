package iris.client_bff.feedback.web;

import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.Email;

/**
 * This class represents the json structure of a feedback request.
 *
 * @author Ostfalia Gruppe 12
 * @author Jens Kutzsche
 */
@Getter
public class FeedbackInputDto {

	private @NonNull String category;

	private @NonNull String title;

	private @NonNull String comment;

	private String name;

	@Email
	private String email;

	private String organisation;

	private String browserName;

	private String browserResolution;
}
