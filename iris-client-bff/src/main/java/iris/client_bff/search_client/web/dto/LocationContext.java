package iris.client_bff.search_client.web.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Ein Standort hat ggf. weitere Informationen wie Tische/Räume, etc.
 */
@JsonPropertyOrder({
	LocationContext.JSON_PROPERTY_ID,
	LocationContext.JSON_PROPERTY_NAME
})
public class LocationContext {
  public static final String JSON_PROPERTY_ID = "id";
  private String id;

  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public LocationContext id(String id) {

	this.id = id;
	return this;
  }

  /**
   * Interne ID des Kontext
   * 
   * @return id
   **/
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getId() {
	return id;
  }

  public void setId(String id) {
	this.id = id;
  }

  public LocationContext name(String name) {

	this.name = name;
	return this;
  }

  /**
   * Bezeichnung
   * 
   * @return name
   **/
  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }

  @Override
  public boolean equals(Object o) {
	if (this == o) {
	  return true;
	}
	if (o == null || getClass() != o.getClass()) {
	  return false;
	}
	LocationContext locationContext = (LocationContext) o;
	return Objects.equals(this.id, locationContext.id) &&
		Objects.equals(this.name, locationContext.name);
  }

  @Override
  public int hashCode() {
	return Objects.hash(id, name);
  }

  @Override
  public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("class LocationContext {\n");
	sb.append("    id: ").append(toIndentedString(id)).append("\n");
	sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
