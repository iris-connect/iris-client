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
 * Gets or Sets WorkEnvironment
 */
public enum WorkEnvironment {
  UNKNOWN("UNKNOWN"),
  OPEN_SPACE_OFFICE("OPEN_SPACE_OFFICE"),
  FOOD_SECTOR("FOOD_SECTOR"),
  BUILDING_SECTOR("BUILDING_SECTOR"),
  LOGISTICS_CENTER("LOGISTICS_CENTER"),
  OTHER("OTHER");

  private String value;

  WorkEnvironment(String value) {
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
  public static WorkEnvironment fromValue(String text) {
    for (WorkEnvironment b : WorkEnvironment.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
