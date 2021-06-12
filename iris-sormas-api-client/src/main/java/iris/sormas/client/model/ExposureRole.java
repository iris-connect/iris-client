/*
 * SORMAS REST API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.61.0-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package iris.sormas.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ExposureRole
 */
public enum ExposureRole {
  PASSENGER("PASSENGER"),
  STAFF("STAFF"),
  NURSING_STAFF("NURSING_STAFF"),
  MEDICAL_STAFF("MEDICAL_STAFF"),
  VISITOR("VISITOR"),
  GUEST("GUEST"),
  CUSTOMER("CUSTOMER"),
  CONSERVATEE("CONSERVATEE"),
  PATIENT("PATIENT"),
  EDUCATOR("EDUCATOR"),
  TRAINEE_TEACHER("TRAINEE_TEACHER"),
  PUPIL("PUPIL"),
  STUDENT("STUDENT"),
  PARENT("PARENT"),
  TEACHER("TEACHER"),
  UNKNOWN("UNKNOWN"),
  OTHER("OTHER");

  private String value;

  ExposureRole(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ExposureRole fromValue(String text) {
    for (ExposureRole b : ExposureRole.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}