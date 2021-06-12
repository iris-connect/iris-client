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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import iris.sormas.client.model.SortProperty;
import iris.sormas.client.model.UserCriteria;
import java.util.ArrayList;
import java.util.List;
/**
 * CriteriaWithSortingUserCriteria
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class CriteriaWithSortingUserCriteria {
  @JsonProperty("criteria")
  private UserCriteria criteria = null;

  @JsonProperty("sortProperties")
  private List<SortProperty> sortProperties = null;

  @JsonProperty("caseCriteria")
  private UserCriteria caseCriteria = null;

  public CriteriaWithSortingUserCriteria criteria(UserCriteria criteria) {
    this.criteria = criteria;
    return this;
  }

   /**
   * Get criteria
   * @return criteria
  **/
  @Schema(description = "")
  public UserCriteria getCriteria() {
    return criteria;
  }

  public void setCriteria(UserCriteria criteria) {
    this.criteria = criteria;
  }

  public CriteriaWithSortingUserCriteria sortProperties(List<SortProperty> sortProperties) {
    this.sortProperties = sortProperties;
    return this;
  }

  public CriteriaWithSortingUserCriteria addSortPropertiesItem(SortProperty sortPropertiesItem) {
    if (this.sortProperties == null) {
      this.sortProperties = new ArrayList<>();
    }
    this.sortProperties.add(sortPropertiesItem);
    return this;
  }

   /**
   * Get sortProperties
   * @return sortProperties
  **/
  @Schema(description = "")
  public List<SortProperty> getSortProperties() {
    return sortProperties;
  }

  public void setSortProperties(List<SortProperty> sortProperties) {
    this.sortProperties = sortProperties;
  }

  public CriteriaWithSortingUserCriteria caseCriteria(UserCriteria caseCriteria) {
    this.caseCriteria = caseCriteria;
    return this;
  }

   /**
   * Get caseCriteria
   * @return caseCriteria
  **/
  @Schema(description = "")
  public UserCriteria getCaseCriteria() {
    return caseCriteria;
  }

  public void setCaseCriteria(UserCriteria caseCriteria) {
    this.caseCriteria = caseCriteria;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CriteriaWithSortingUserCriteria criteriaWithSortingUserCriteria = (CriteriaWithSortingUserCriteria) o;
    return Objects.equals(this.criteria, criteriaWithSortingUserCriteria.criteria) &&
        Objects.equals(this.sortProperties, criteriaWithSortingUserCriteria.sortProperties) &&
        Objects.equals(this.caseCriteria, criteriaWithSortingUserCriteria.caseCriteria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(criteria, sortProperties, caseCriteria);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CriteriaWithSortingUserCriteria {\n");
    
    sb.append("    criteria: ").append(toIndentedString(criteria)).append("\n");
    sb.append("    sortProperties: ").append(toIndentedString(sortProperties)).append("\n");
    sb.append("    caseCriteria: ").append(toIndentedString(caseCriteria)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}