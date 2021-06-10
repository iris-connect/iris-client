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
 * Gets or Sets UserRight
 */
public enum UserRight {
  CASE_CREATE("CASE_CREATE"),
  CASE_VIEW("CASE_VIEW"),
  CASE_EDIT("CASE_EDIT"),
  CASE_TRANSFER("CASE_TRANSFER"),
  CASE_REFER_FROM_POE("CASE_REFER_FROM_POE"),
  CASE_INVESTIGATE("CASE_INVESTIGATE"),
  CASE_CLASSIFY("CASE_CLASSIFY"),
  CASE_CHANGE_DISEASE("CASE_CHANGE_DISEASE"),
  CASE_CHANGE_EPID_NUMBER("CASE_CHANGE_EPID_NUMBER"),
  CASE_DELETE("CASE_DELETE"),
  CASE_IMPORT("CASE_IMPORT"),
  CASE_EXPORT("CASE_EXPORT"),
  CASE_SHARE("CASE_SHARE"),
  CASE_ARCHIVE("CASE_ARCHIVE"),
  CASE_VIEW_ARCHIVED("CASE_VIEW_ARCHIVED"),
  CASE_MERGE("CASE_MERGE"),
  PERSON_VIEW("PERSON_VIEW"),
  PERSON_EDIT("PERSON_EDIT"),
  PERSON_DELETE("PERSON_DELETE"),
  PERSON_CONTACT_DETAILS_DELETE("PERSON_CONTACT_DETAILS_DELETE"),
  SAMPLE_CREATE("SAMPLE_CREATE"),
  SAMPLE_VIEW("SAMPLE_VIEW"),
  SAMPLE_EDIT("SAMPLE_EDIT"),
  SAMPLE_EDIT_NOT_OWNED("SAMPLE_EDIT_NOT_OWNED"),
  SAMPLE_DELETE("SAMPLE_DELETE"),
  SAMPLE_TRANSFER("SAMPLE_TRANSFER"),
  SAMPLE_EXPORT("SAMPLE_EXPORT"),
  SAMPLE_VIEW_ARCHIVED("SAMPLE_VIEW_ARCHIVED"),
  PATHOGEN_TEST_CREATE("PATHOGEN_TEST_CREATE"),
  PATHOGEN_TEST_EDIT("PATHOGEN_TEST_EDIT"),
  PATHOGEN_TEST_DELETE("PATHOGEN_TEST_DELETE"),
  ADDITIONAL_TEST_VIEW("ADDITIONAL_TEST_VIEW"),
  ADDITIONAL_TEST_CREATE("ADDITIONAL_TEST_CREATE"),
  ADDITIONAL_TEST_EDIT("ADDITIONAL_TEST_EDIT"),
  ADDITIONAL_TEST_DELETE("ADDITIONAL_TEST_DELETE"),
  CONTACT_CREATE("CONTACT_CREATE"),
  CONTACT_IMPORT("CONTACT_IMPORT"),
  CONTACT_VIEW("CONTACT_VIEW"),
  CONTACT_ASSIGN("CONTACT_ASSIGN"),
  CONTACT_EDIT("CONTACT_EDIT"),
  CONTACT_DELETE("CONTACT_DELETE"),
  CONTACT_CLASSIFY("CONTACT_CLASSIFY"),
  CONTACT_CONVERT("CONTACT_CONVERT"),
  CONTACT_EXPORT("CONTACT_EXPORT"),
  CONTACT_VIEW_ARCHIVED("CONTACT_VIEW_ARCHIVED"),
  CONTACT_REASSIGN_CASE("CONTACT_REASSIGN_CASE"),
  CONTACT_MERGE("CONTACT_MERGE"),
  MANAGE_EXTERNAL_SYMPTOM_JOURNAL("MANAGE_EXTERNAL_SYMPTOM_JOURNAL"),
  VISIT_CREATE("VISIT_CREATE"),
  VISIT_EDIT("VISIT_EDIT"),
  VISIT_DELETE("VISIT_DELETE"),
  VISIT_EXPORT("VISIT_EXPORT"),
  TASK_CREATE("TASK_CREATE"),
  TASK_VIEW("TASK_VIEW"),
  TASK_EDIT("TASK_EDIT"),
  TASK_ASSIGN("TASK_ASSIGN"),
  TASK_VIEW_ARCHIVED("TASK_VIEW_ARCHIVED"),
  TASK_DELETE("TASK_DELETE"),
  TASK_EXPORT("TASK_EXPORT"),
  ACTION_CREATE("ACTION_CREATE"),
  ACTION_DELETE("ACTION_DELETE"),
  ACTION_EDIT("ACTION_EDIT"),
  EVENT_CREATE("EVENT_CREATE"),
  EVENT_VIEW("EVENT_VIEW"),
  EVENT_EDIT("EVENT_EDIT"),
  EVENT_IMPORT("EVENT_IMPORT"),
  EVENT_EXPORT("EVENT_EXPORT"),
  EVENT_ARCHIVE("EVENT_ARCHIVE"),
  EVENT_DELETE("EVENT_DELETE"),
  EVENT_VIEW_ARCHIVED("EVENT_VIEW_ARCHIVED"),
  EVENTPARTICIPANT_CREATE("EVENTPARTICIPANT_CREATE"),
  EVENTPARTICIPANT_EDIT("EVENTPARTICIPANT_EDIT"),
  EVENTPARTICIPANT_DELETE("EVENTPARTICIPANT_DELETE"),
  EVENTPARTICIPANT_IMPORT("EVENTPARTICIPANT_IMPORT"),
  EVENTGROUP_CREATE("EVENTGROUP_CREATE"),
  EVENTGROUP_EDIT("EVENTGROUP_EDIT"),
  EVENTGROUP_LINK("EVENTGROUP_LINK"),
  EVENTGROUP_ARCHIVE("EVENTGROUP_ARCHIVE"),
  EVENTGROUP_DELETE("EVENTGROUP_DELETE"),
  WEEKLYREPORT_CREATE("WEEKLYREPORT_CREATE"),
  WEEKLYREPORT_VIEW("WEEKLYREPORT_VIEW"),
  USER_CREATE("USER_CREATE"),
  USER_EDIT("USER_EDIT"),
  USER_VIEW("USER_VIEW"),
  CONFIGURATION_ACCESS("CONFIGURATION_ACCESS"),
  OUTBREAK_CONFIGURE_ALL("OUTBREAK_CONFIGURE_ALL"),
  OUTBREAK_CONFIGURE_RESTRICTED("OUTBREAK_CONFIGURE_RESTRICTED"),
  SEND_MANUAL_EXTERNAL_MESSAGES("SEND_MANUAL_EXTERNAL_MESSAGES"),
  STATISTICS_ACCESS("STATISTICS_ACCESS"),
  STATISTICS_EXPORT("STATISTICS_EXPORT"),
  DATABASE_EXPORT_ACCESS("DATABASE_EXPORT_ACCESS"),
  PERFORM_BULK_OPERATIONS("PERFORM_BULK_OPERATIONS"),
  MANAGE_PUBLIC_EXPORT_CONFIGURATION("MANAGE_PUBLIC_EXPORT_CONFIGURATION"),
  PERFORM_BULK_OPERATIONS_CASE_SAMPLES("PERFORM_BULK_OPERATIONS_CASE_SAMPLES"),
  INFRASTRUCTURE_CREATE("INFRASTRUCTURE_CREATE"),
  INFRASTRUCTURE_EDIT("INFRASTRUCTURE_EDIT"),
  INFRASTRUCTURE_VIEW("INFRASTRUCTURE_VIEW"),
  INFRASTRUCTURE_VIEW_ARCHIVED("INFRASTRUCTURE_VIEW_ARCHIVED"),
  INFRASTRUCTURE_EXPORT("INFRASTRUCTURE_EXPORT"),
  INFRASTRUCTURE_IMPORT("INFRASTRUCTURE_IMPORT"),
  INFRASTRUCTURE_ARCHIVE("INFRASTRUCTURE_ARCHIVE"),
  USER_RIGHTS_MANAGE("USER_RIGHTS_MANAGE"),
  DASHBOARD_SURVEILLANCE_ACCESS("DASHBOARD_SURVEILLANCE_ACCESS"),
  DASHBOARD_CONTACT_ACCESS("DASHBOARD_CONTACT_ACCESS"),
  DASHBOARD_CONTACT_VIEW_TRANSMISSION_CHAINS("DASHBOARD_CONTACT_VIEW_TRANSMISSION_CHAINS"),
  DASHBOARD_CAMPAIGNS_ACCESS("DASHBOARD_CAMPAIGNS_ACCESS"),
  CASE_MANAGEMENT_ACCESS("CASE_MANAGEMENT_ACCESS"),
  THERAPY_VIEW("THERAPY_VIEW"),
  PRESCRIPTION_CREATE("PRESCRIPTION_CREATE"),
  PRESCRIPTION_EDIT("PRESCRIPTION_EDIT"),
  PRESCRIPTION_DELETE("PRESCRIPTION_DELETE"),
  TREATMENT_CREATE("TREATMENT_CREATE"),
  TREATMENT_EDIT("TREATMENT_EDIT"),
  TREATMENT_DELETE("TREATMENT_DELETE"),
  CLINICAL_COURSE_VIEW("CLINICAL_COURSE_VIEW"),
  CLINICAL_COURSE_EDIT("CLINICAL_COURSE_EDIT"),
  CLINICAL_VISIT_CREATE("CLINICAL_VISIT_CREATE"),
  CLINICAL_VISIT_EDIT("CLINICAL_VISIT_EDIT"),
  CLINICAL_VISIT_DELETE("CLINICAL_VISIT_DELETE"),
  PORT_HEALTH_INFO_VIEW("PORT_HEALTH_INFO_VIEW"),
  PORT_HEALTH_INFO_EDIT("PORT_HEALTH_INFO_EDIT"),
  POPULATION_MANAGE("POPULATION_MANAGE"),
  DOCUMENT_TEMPLATE_MANAGEMENT("DOCUMENT_TEMPLATE_MANAGEMENT"),
  QUARANTINE_ORDER_CREATE("QUARANTINE_ORDER_CREATE"),
  LINE_LISTING_CONFIGURE("LINE_LISTING_CONFIGURE"),
  LINE_LISTING_CONFIGURE_NATION("LINE_LISTING_CONFIGURE_NATION"),
  AGGREGATE_REPORT_VIEW("AGGREGATE_REPORT_VIEW"),
  AGGREGATE_REPORT_EXPORT("AGGREGATE_REPORT_EXPORT"),
  AGGREGATE_REPORT_EDIT("AGGREGATE_REPORT_EDIT"),
  SEE_PERSONAL_DATA_IN_JURISDICTION("SEE_PERSONAL_DATA_IN_JURISDICTION"),
  SEE_PERSONAL_DATA_OUTSIDE_JURISDICTION("SEE_PERSONAL_DATA_OUTSIDE_JURISDICTION"),
  SEE_SENSITIVE_DATA_IN_JURISDICTION("SEE_SENSITIVE_DATA_IN_JURISDICTION"),
  SEE_SENSITIVE_DATA_OUTSIDE_JURISDICTION("SEE_SENSITIVE_DATA_OUTSIDE_JURISDICTION"),
  CAMPAIGN_VIEW("CAMPAIGN_VIEW"),
  CAMPAIGN_EDIT("CAMPAIGN_EDIT"),
  CAMPAIGN_ARCHIVE("CAMPAIGN_ARCHIVE"),
  CAMPAIGN_DELETE("CAMPAIGN_DELETE"),
  CAMPAIGN_FORM_DATA_VIEW("CAMPAIGN_FORM_DATA_VIEW"),
  CAMPAIGN_FORM_DATA_EDIT("CAMPAIGN_FORM_DATA_EDIT"),
  CAMPAIGN_FORM_DATA_ARCHIVE("CAMPAIGN_FORM_DATA_ARCHIVE"),
  CAMPAIGN_FORM_DATA_DELETE("CAMPAIGN_FORM_DATA_DELETE"),
  CAMPAIGN_FORM_DATA_EXPORT("CAMPAIGN_FORM_DATA_EXPORT"),
  BAG_EXPORT("BAG_EXPORT"),
  SORMAS_TO_SORMAS_SHARE("SORMAS_TO_SORMAS_SHARE"),
  LAB_MESSAGES("LAB_MESSAGES"),
  PERFORM_BULK_OPERATIONS_LAB_MESSAGES("PERFORM_BULK_OPERATIONS_LAB_MESSAGES");

  private String value;

  UserRight(String value) {
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
  public static UserRight fromValue(String text) {
    for (UserRight b : UserRight.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
