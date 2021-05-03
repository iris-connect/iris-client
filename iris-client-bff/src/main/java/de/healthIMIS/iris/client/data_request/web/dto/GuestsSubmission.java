package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * GuestsSubmission
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class GuestsSubmission   {
  @JsonProperty("secret")
  private String secret;

  @JsonProperty("keyReference")
  private String keyReference;

  @JsonProperty("encryptedData")
  private GuestList encryptedData;

  public GuestsSubmission secret(String secret) {
    this.secret = secret;
    return this;
  }

  /**
   * The encrypted secret key for encryption. (`keyToTransport` in the general description of the API.)
   * @return secret
  */
  @ApiModelProperty(required = true, value = "The encrypted secret key for encryption. (`keyToTransport` in the general description of the API.)")
  @NotNull


  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public GuestsSubmission keyReference(String keyReference) {
    this.keyReference = keyReference;
    return this;
  }

  /**
   * Reference to the used encryption key. This must be the value from keyReference of the DataRequest as this matches the passed and thus used key.
   * @return keyReference
  */
  @ApiModelProperty(required = true, value = "Reference to the used encryption key. This must be the value from keyReference of the DataRequest as this matches the passed and thus used key.")
  @NotNull


  public String getKeyReference() {
    return keyReference;
  }

  public void setKeyReference(String keyReference) {
    this.keyReference = keyReference;
  }

  public GuestsSubmission encryptedData(GuestList encryptedData) {
    this.encryptedData = encryptedData;
    return this;
  }

  /**
   * Get encryptedData
   * @return encryptedData
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public GuestList getEncryptedData() {
    return encryptedData;
  }

  public void setEncryptedData(GuestList encryptedData) {
    this.encryptedData = encryptedData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestsSubmission guestsSubmission = (GuestsSubmission) o;
    return Objects.equals(this.secret, guestsSubmission.secret) &&
        Objects.equals(this.keyReference, guestsSubmission.keyReference) &&
        Objects.equals(this.encryptedData, guestsSubmission.encryptedData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(secret, keyReference, encryptedData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestsSubmission {\n");
    
    sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
    sb.append("    keyReference: ").append(toIndentedString(keyReference)).append("\n");
    sb.append("    encryptedData: ").append(toIndentedString(encryptedData)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

