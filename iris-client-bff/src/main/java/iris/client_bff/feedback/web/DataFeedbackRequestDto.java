package iris.client_bff.feedback.web;

import lombok.NonNull;

/*
 * This class represents the json structure of a request to EPS.
 * It is also the input json of Iris-Client-Frontend uses this structure.
 * @author Ostfalia Gruppe 12
 */
public class DataFeedbackRequestDto {

	private @NonNull String category;

	private @NonNull String title;

	private @NonNull String comment;

	private String name;

	private String email;

	private String organisation;

	private String browserName;

	private String browserResolution;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setCommentar(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getBrowserResolution() {
		return browserResolution;
	}

	public void setBrowserResolution(String browserResolution) {
		this.browserResolution = browserResolution;
	}
}
