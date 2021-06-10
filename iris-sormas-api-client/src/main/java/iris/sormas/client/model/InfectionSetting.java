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
 * Gets or Sets InfectionSetting
 */
public enum InfectionSetting {
  UNKNOWN("UNKNOWN"),
  AMBULATORY("AMBULATORY"),
  MEDICAL_PRACTICE("MEDICAL_PRACTICE"),
  OPERATIVE_1200("OPERATIVE_1200"),
  HOSPITAL_1300("HOSPITAL_1300"),
  OTHER_OUTPATIENT_FACILITY("OTHER_OUTPATIENT_FACILITY"),
  STATIONARY("STATIONARY"),
  HOSPITAL_2100("HOSPITAL_2100"),
  NORMAL_WARD("NORMAL_WARD"),
  OPERATIVE_2111("OPERATIVE_2111"),
  NOT_OPERATIVE("NOT_OPERATIVE"),
  HEMATOLOGICAL_ONCOLOGY("HEMATOLOGICAL_ONCOLOGY"),
  CHILDREN_WARD("CHILDREN_WARD"),
  NEONATOLOGY("NEONATOLOGY"),
  INTENSIVE_CARE_UNIT("INTENSIVE_CARE_UNIT"),
  OTHER_STATION("OTHER_STATION"),
  NURSING_HOME("NURSING_HOME"),
  REHAB_FACILITY("REHAB_FACILITY"),
  OTHER_STATIONARY_FACILITY("OTHER_STATIONARY_FACILITY");

  private String value;

  InfectionSetting(String value) {
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
  public static InfectionSetting fromValue(String text) {
    for (InfectionSetting b : InfectionSetting.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
