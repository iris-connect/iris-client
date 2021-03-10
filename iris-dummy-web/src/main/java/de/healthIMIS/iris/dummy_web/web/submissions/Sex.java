package de.healthIMIS.iris.dummy_web.web.submissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Sex
 */
public enum Sex {
  MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER"),
    UNKNOWN("UNKNOWN");

  private String value;

  Sex(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Sex fromValue(String text) {
    for (Sex b : Sex.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
