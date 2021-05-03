package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ExistingDataRequestClientWithLocationList
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class ExistingDataRequestClientWithLocationList   {
  @JsonProperty("dataRequests")
  @Valid
  private List<ExistingDataRequestClientWithLocation> dataRequests = null;

  public ExistingDataRequestClientWithLocationList dataRequests(List<ExistingDataRequestClientWithLocation> dataRequests) {
    this.dataRequests = dataRequests;
    return this;
  }

  public ExistingDataRequestClientWithLocationList addDataRequestsItem(ExistingDataRequestClientWithLocation dataRequestsItem) {
    if (this.dataRequests == null) {
      this.dataRequests = new ArrayList<>();
    }
    this.dataRequests.add(dataRequestsItem);
    return this;
  }

  /**
   * Get dataRequests
   * @return dataRequests
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<ExistingDataRequestClientWithLocation> getDataRequests() {
    return dataRequests;
  }

  public void setDataRequests(List<ExistingDataRequestClientWithLocation> dataRequests) {
    this.dataRequests = dataRequests;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExistingDataRequestClientWithLocationList existingDataRequestClientWithLocationList = (ExistingDataRequestClientWithLocationList) o;
    return Objects.equals(this.dataRequests, existingDataRequestClientWithLocationList.dataRequests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataRequests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExistingDataRequestClientWithLocationList {\n");
    
    sb.append("    dataRequests: ").append(toIndentedString(dataRequests)).append("\n");
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

