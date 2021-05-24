package iris.client_bff.feedback_api;

import io.swagger.annotations.ApiModel;
import lombok.NonNull;
import lombok.ToString;

/*
 * This class represents the json structure of a request to Iris-Public-Server.
 * It is also the incoming json of Iris-Client-Frontend uses this structure.
 * @author Ostfalia Gruppe 12
 */
@ApiModel(description = "The data request that will be sent by the FE for feedback.")
@ToString
public class DataFeedbackRequestDto {

	private @NonNull String categorie;
	
	private @NonNull String title;
	
	private @NonNull String commentar;

	private String name;

	private String email;

	private String organisation;

	private String browserName;

	private String browserResolution;

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCommentar() {
		return commentar;
	}

	public void setCommentar(String commentar) {
		this.commentar = commentar;
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
