package iris.client_bff.data_request.web.dto;

import io.swagger.annotations.ApiModel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Additional informations about the work place of the contact person.
 */
@ApiModel(description = "Additional informations about the work place of the contact person.")
public class ContactPersonAllOfWorkPlace {

	@JsonProperty("name")
	private String name;

	@JsonProperty("pointOfContact")
	private String pointOfContact;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("address")
	private Address address;

}
