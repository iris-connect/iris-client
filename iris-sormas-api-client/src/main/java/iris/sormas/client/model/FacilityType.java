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
 * Gets or Sets FacilityType
 */
public enum FacilityType {
  ASSOCIATION("ASSOCIATION"),
  BUSINESS("BUSINESS"),
  BAR("BAR"),
  CAMPSITE("CAMPSITE"),
  CANTINE("CANTINE"),
  CHILDRENS_DAY_CARE("CHILDRENS_DAY_CARE"),
  CHILDRENS_HOME("CHILDRENS_HOME"),
  CORRECTIONAL_FACILITY("CORRECTIONAL_FACILITY"),
  CRUISE_SHIP("CRUISE_SHIP"),
  ELDERLY_DAY_CARE("ELDERLY_DAY_CARE"),
  EVENT_VENUE("EVENT_VENUE"),
  FOOD_STALL("FOOD_STALL"),
  HOLIDAY_CAMP("HOLIDAY_CAMP"),
  HOMELESS_SHELTER("HOMELESS_SHELTER"),
  HOSPITAL("HOSPITAL"),
  HOSTEL("HOSTEL"),
  HOTEL("HOTEL"),
  KINDERGARTEN("KINDERGARTEN"),
  LABORATORY("LABORATORY"),
  MASS_ACCOMMODATION("MASS_ACCOMMODATION"),
  MILITARY_BARRACKS("MILITARY_BARRACKS"),
  MOBILE_NURSING_SERVICE("MOBILE_NURSING_SERVICE"),
  NIGHT_CLUB("NIGHT_CLUB"),
  OTHER_ACCOMMODATION("OTHER_ACCOMMODATION"),
  OTHER_CARE_FACILITY("OTHER_CARE_FACILITY"),
  OTHER_CATERING_OUTLET("OTHER_CATERING_OUTLET"),
  OTHER_EDUCATIONAL_FACILITY("OTHER_EDUCATIONAL_FACILITY"),
  OTHER_LEISURE_FACILITY("OTHER_LEISURE_FACILITY"),
  OTHER_MEDICAL_FACILITY("OTHER_MEDICAL_FACILITY"),
  OTHER_RESIDENCE("OTHER_RESIDENCE"),
  OTHER_WORKING_PLACE("OTHER_WORKING_PLACE"),
  OTHER_COMMERCE("OTHER_COMMERCE"),
  OUTPATIENT_TREATMENT_FACILITY("OUTPATIENT_TREATMENT_FACILITY"),
  PLACE_OF_WORSHIP("PLACE_OF_WORSHIP"),
  PUBLIC_PLACE("PUBLIC_PLACE"),
  REFUGEE_ACCOMMODATION("REFUGEE_ACCOMMODATION"),
  REHAB_FACILITY("REHAB_FACILITY"),
  RESTAURANT("RESTAURANT"),
  RETIREMENT_HOME("RETIREMENT_HOME"),
  RETAIL("RETAIL"),
  WHOLESALE("WHOLESALE"),
  SCHOOL("SCHOOL"),
  SWIMMING_POOL("SWIMMING_POOL"),
  THEATER("THEATER"),
  UNIVERSITY("UNIVERSITY"),
  ZOO("ZOO"),
  AMBULATORY_SURGERY_FACILITY("AMBULATORY_SURGERY_FACILITY"),
  DIALYSIS_FACILITY("DIALYSIS_FACILITY"),
  DAY_HOSPITAL("DAY_HOSPITAL"),
  MATERNITY_FACILITY("MATERNITY_FACILITY"),
  MEDICAL_PRACTICE("MEDICAL_PRACTICE"),
  DENTAL_PRACTICE("DENTAL_PRACTICE"),
  OTHER_MEDICAL_PRACTICE("OTHER_MEDICAL_PRACTICE"),
  DIAGNOSTIC_PREVENTATIVE_THERAPEUTIC_FACILITY("DIAGNOSTIC_PREVENTATIVE_THERAPEUTIC_FACILITY"),
  EMERGENCY_MEDICAL_SERVICES("EMERGENCY_MEDICAL_SERVICES"),
  ELDERLY_CARE_FACILITY("ELDERLY_CARE_FACILITY"),
  DISABLED_PERSON_HABITATION("DISABLED_PERSON_HABITATION"),
  CARE_RECIPIENT_HABITATION("CARE_RECIPIENT_HABITATION"),
  VISITING_AMBULATORY_AID("VISITING_AMBULATORY_AID"),
  AFTER_SCHOOL("AFTER_SCHOOL");

  private String value;

  FacilityType(String value) {
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
  public static FacilityType fromValue(String text) {
    for (FacilityType b : FacilityType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}