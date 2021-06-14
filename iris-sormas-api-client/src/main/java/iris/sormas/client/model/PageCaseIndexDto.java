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
import iris.sormas.client.model.CaseIndexDto;
import java.util.ArrayList;
import java.util.List;
/**
 * PageCaseIndexDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-08T06:53:38.737461+02:00[Europe/Berlin]")
public class PageCaseIndexDto {
  @JsonProperty("elements")
  private List<CaseIndexDto> elements = null;

  @JsonProperty("offset")
  private Integer offset = null;

  @JsonProperty("size")
  private Integer size = null;

  @JsonProperty("totalElementCount")
  private Long totalElementCount = null;

  @JsonProperty("hasNext")
  private Boolean hasNext = null;

  public PageCaseIndexDto elements(List<CaseIndexDto> elements) {
    this.elements = elements;
    return this;
  }

  public PageCaseIndexDto addElementsItem(CaseIndexDto elementsItem) {
    if (this.elements == null) {
      this.elements = new ArrayList<>();
    }
    this.elements.add(elementsItem);
    return this;
  }

   /**
   * Get elements
   * @return elements
  **/
  @Schema(description = "")
  public List<CaseIndexDto> getElements() {
    return elements;
  }

  public void setElements(List<CaseIndexDto> elements) {
    this.elements = elements;
  }

  public PageCaseIndexDto offset(Integer offset) {
    this.offset = offset;
    return this;
  }

   /**
   * Get offset
   * @return offset
  **/
  @Schema(description = "")
  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public PageCaseIndexDto size(Integer size) {
    this.size = size;
    return this;
  }

   /**
   * Get size
   * @return size
  **/
  @Schema(description = "")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public PageCaseIndexDto totalElementCount(Long totalElementCount) {
    this.totalElementCount = totalElementCount;
    return this;
  }

   /**
   * Get totalElementCount
   * @return totalElementCount
  **/
  @Schema(description = "")
  public Long getTotalElementCount() {
    return totalElementCount;
  }

  public void setTotalElementCount(Long totalElementCount) {
    this.totalElementCount = totalElementCount;
  }

  public PageCaseIndexDto hasNext(Boolean hasNext) {
    this.hasNext = hasNext;
    return this;
  }

   /**
   * Get hasNext
   * @return hasNext
  **/
  @Schema(description = "")
  public Boolean isHasNext() {
    return hasNext;
  }

  public void setHasNext(Boolean hasNext) {
    this.hasNext = hasNext;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageCaseIndexDto pageCaseIndexDto = (PageCaseIndexDto) o;
    return Objects.equals(this.elements, pageCaseIndexDto.elements) &&
        Objects.equals(this.offset, pageCaseIndexDto.offset) &&
        Objects.equals(this.size, pageCaseIndexDto.size) &&
        Objects.equals(this.totalElementCount, pageCaseIndexDto.totalElementCount) &&
        Objects.equals(this.hasNext, pageCaseIndexDto.hasNext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elements, offset, size, totalElementCount, hasNext);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageCaseIndexDto {\n");
    
    sb.append("    elements: ").append(toIndentedString(elements)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    totalElementCount: ").append(toIndentedString(totalElementCount)).append("\n");
    sb.append("    hasNext: ").append(toIndentedString(hasNext)).append("\n");
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
