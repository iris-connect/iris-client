package iris.client_bff.feedback;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * @author Jens Kutzsche
 */
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class FeedbackOutputDto {

	private @NonNull String category;

	private @NonNull String title;

	private @NonNull String comment;

	private String name;

	@Email
	private String email;

	private String organisation;

	private String browserName;

	private String browserResolution;

	private @NonNull @Setter String sourceApp;

	private @NonNull @Setter String appVersion;
}
