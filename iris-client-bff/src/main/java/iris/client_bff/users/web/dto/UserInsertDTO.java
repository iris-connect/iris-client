package iris.client_bff.users.web.dto;

import iris.client_bff.core.validation.AttackDetector.Password;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.users.web.ValidPassword;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
		UserInsertDTO.JSON_PROPERTY_FIRST_NAME,
		UserInsertDTO.JSON_PROPERTY_LAST_NAME,
		UserInsertDTO.JSON_PROPERTY_USER_NAME,
		UserInsertDTO.JSON_PROPERTY_PASSWORD,
		UserInsertDTO.JSON_PROPERTY_ROLE
})
public class UserInsertDTO {
	public static final String JSON_PROPERTY_FIRST_NAME = "firstName";
	@Size(max = 200)
	@NoSignOfAttack
	private String firstName;

	public static final String JSON_PROPERTY_LAST_NAME = "lastName";
	@Size(max = 200)
	@NoSignOfAttack
	private String lastName;

	public static final String JSON_PROPERTY_USER_NAME = "userName";
	@NotBlank
	@Size(max = 50)
	@NoSignOfAttack
	private String userName;

	public static final String JSON_PROPERTY_PASSWORD = "password";
	@ValidPassword
	@NotBlank
	@Size(max = 200)
	@NoSignOfAttack(payload = Password.class)
	private String password;

	public static final String JSON_PROPERTY_ROLE = "role";
	@NotNull
	private UserRoleDTO role;

	public UserInsertDTO firstName(String firstName) {

		this.firstName = firstName;
		return this;
	}

	/**
	 * Get firstName
	 *
	 * @return firstName
	 **/
	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_FIRST_NAME)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserInsertDTO lastName(String lastName) {

		this.lastName = lastName;
		return this;
	}

	/**
	 * Get lastName
	 *
	 * @return lastName
	 **/
	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_LAST_NAME)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserInsertDTO userName(String userName) {

		this.userName = userName;
		return this;
	}

	/**
	 * Get userName
	 *
	 * @return userName
	 **/
	@JsonProperty(JSON_PROPERTY_USER_NAME)
	@JsonInclude(value = JsonInclude.Include.ALWAYS)

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserInsertDTO password(String password) {

		this.password = password;
		return this;
	}

	/**
	 * Get password
	 *
	 * @return password
	 **/
	@JsonProperty(JSON_PROPERTY_PASSWORD)
	@JsonInclude(value = JsonInclude.Include.ALWAYS)

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserInsertDTO role(UserRoleDTO role) {

		this.role = role;
		return this;
	}

	/**
	 * Get role
	 *
	 * @return role
	 **/
	@JsonProperty(JSON_PROPERTY_ROLE)
	@JsonInclude(value = JsonInclude.Include.ALWAYS)

	public UserRoleDTO getRole() {
		return role;
	}

	public void setRole(UserRoleDTO role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserInsertDTO userInsert = (UserInsertDTO) o;
		return Objects.equals(this.firstName, userInsert.firstName) &&
				Objects.equals(this.lastName, userInsert.lastName) &&
				Objects.equals(this.userName, userInsert.userName) &&
				Objects.equals(this.password, userInsert.password) &&
				Objects.equals(this.role, userInsert.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, userName, password, role);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserInsert {\n");
		sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
		sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
		sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
		sb.append("    password: ").append(toIndentedString(password)).append("\n");
		sb.append("    role: ").append(toIndentedString(role)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
