package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Basic data type of a data submission which contains the unencrypted metadata needed for processing.
 */
@ApiModel(description = "Basic data type of a data submission which contains the unencrypted metadata needed for processing.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class DataSubmission   {
  @JsonProperty("secret")
  private String secret;

  @JsonProperty("keyReference")
  private String keyReference;

  public DataSubmission secret(String secret) {
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

  public DataSubmission keyReference(String keyReference) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataSubmission dataSubmission = (DataSubmission) o;
    return Objects.equals(this.secret, dataSubmission.secret) &&
        Objects.equals(this.keyReference, dataSubmission.keyReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(secret, keyReference);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataSubmission {\n");
    
    sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
    sb.append("    keyReference: ").append(toIndentedString(keyReference)).append("\n");
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

