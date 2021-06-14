package iris.client_bff.search_client.web.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	LocationInformation.JSON_PROPERTY_ID,
	LocationInformation.JSON_PROPERTY_PROVIDER_ID,
	LocationInformation.JSON_PROPERTY_NAME,
	LocationInformation.JSON_PROPERTY_PUBLIC_KEY,
	LocationInformation.JSON_PROPERTY_CONTACT,
	LocationInformation.JSON_PROPERTY_CONTEXTS
})
public class LocationInformation {
  public static final String JSON_PROPERTY_ID = "id";
  private String id;

  public static final String JSON_PROPERTY_PROVIDER_ID = "providerId";
  private String providerId;

  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_PUBLIC_KEY = "publicKey";
  private String publicKey;

  public static final String JSON_PROPERTY_CONTACT = "contact";
  private LocationContact contact;

  public static final String JSON_PROPERTY_CONTEXTS = "contexts";
  private List<LocationContext> contexts = null;

  public LocationInformation id(String id) {

	this.id = id;
	return this;
  }

  /**
   * Interne ID (beim Provider)
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

  public LocationInformation providerId(String providerId) {

	this.providerId = providerId;
	return this;
  }

  /**
   * ID des App providers
   * 
   * @return providerId
   **/
  @JsonProperty(JSON_PROPERTY_PROVIDER_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getProviderId() {
	return providerId;
  }

  public void setProviderId(String providerId) {
	this.providerId = providerId;
  }

  public LocationInformation name(String name) {

	this.name = name;
	return this;
  }

  /**
   * Name des Standorts
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

  public LocationInformation publicKey(String publicKey) {

	this.publicKey = publicKey;
	return this;
  }

  /**
   * Öffentlicher Schlüssel, ggf. für Nachrichtenaustausch
   * 
   * @return publicKey
   **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PUBLIC_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPublicKey() {
	return publicKey;
  }

  public void setPublicKey(String publicKey) {
	this.publicKey = publicKey;
  }

  public LocationInformation contact(LocationContact contact) {

	this.contact = contact;
	return this;
  }

  /**
   * Get contact
   * 
   * @return contact
   **/
  @JsonProperty(JSON_PROPERTY_CONTACT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocationContact getContact() {
	return contact;
  }

  public void setContact(LocationContact contact) {
	this.contact = contact;
  }

  public LocationInformation contexts(List<LocationContext> contexts) {

	this.contexts = contexts;
	return this;
  }

  public LocationInformation addContextsItem(LocationContext contextsItem) {
	if (this.contexts == null) {
	  this.contexts = new ArrayList<>();
	}
	this.contexts.add(contextsItem);
	return this;
  }

  /**
   * Get contexts
   * 
   * @return contexts
   **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONTEXTS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<LocationContext> getContexts() {
	return contexts;
  }

  public void setContexts(List<LocationContext> contexts) {
	this.contexts = contexts;
  }

  @Override
  public boolean equals(Object o) {
	if (this == o) {
	  return true;
	}
	if (o == null || getClass() != o.getClass()) {
	  return false;
	}
	LocationInformation locationInformation = (LocationInformation) o;
	return Objects.equals(this.id, locationInformation.id) &&
		Objects.equals(this.providerId, locationInformation.providerId) &&
		Objects.equals(this.name, locationInformation.name) &&
		Objects.equals(this.publicKey, locationInformation.publicKey) &&
		Objects.equals(this.contact, locationInformation.contact) &&
		Objects.equals(this.contexts, locationInformation.contexts);
  }

  @Override
  public int hashCode() {
	return Objects.hash(id, providerId, name, publicKey, contact, contexts);
  }

  @Override
  public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("class LocationInformation {\n");
	sb.append("    id: ").append(toIndentedString(id)).append("\n");
	sb.append("    providerId: ").append(toIndentedString(providerId)).append("\n");
	sb.append("    name: ").append(toIndentedString(name)).append("\n");
	sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
	sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
	sb.append("    contexts: ").append(toIndentedString(contexts)).append("\n");
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
