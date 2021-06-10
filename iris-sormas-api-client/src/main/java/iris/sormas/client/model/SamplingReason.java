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
 * Gets or Sets SamplingReason
 */
public enum SamplingReason {
  PRESENCE_OF_SYMPTOMS("PRESENCE_OF_SYMPTOMS"),
  OUTBREAK("OUTBREAK"),
  SCREENING("SCREENING"),
  PROFESSIONAL_REASON("PROFESSIONAL_REASON"),
  QUARANTINE_REGULATIONS("QUARANTINE_REGULATIONS"),
  CONTACT_TO_CASE("CONTACT_TO_CASE"),
  PLANNING_TO_TRAVEL("PLANNING_TO_TRAVEL"),
  RETURNING_TRAVELER("RETURNING_TRAVELER"),
  PERSONAL_REASON("PERSONAL_REASON"),
  MOVING_RETURNING_RETIREMENT_HOME("MOVING_RETURNING_RETIREMENT_HOME"),
  SWISS_COVID_APP_NOTIFICATION("SWISS_COVID_APP_NOTIFICATION"),
  QUARANTINE_END("QUARANTINE_END"),
  UNKNOWN("UNKNOWN"),
  OTHER_REASON("OTHER_REASON");

  private String value;

  SamplingReason(String value) {
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
  public static SamplingReason fromValue(String text) {
    for (SamplingReason b : SamplingReason.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
