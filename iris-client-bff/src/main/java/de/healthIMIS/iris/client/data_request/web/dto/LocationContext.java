package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Ein Standort hat ggf. weitere Informationen wie Tische/Räume, etc.
 */
@ApiModel(description = "Ein Standort hat ggf. weitere Informationen wie Tische/Räume, etc.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-29T21:00:09.691126+01:00[Europe/London]")
public class LocationContext   {
  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  public LocationContext id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Interne ID des Kontext
   * @return id
  */
  @ApiModelProperty(example = "5f4bfff742c1bf5f72918851", required = true, value = "Interne ID des Kontext")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocationContext name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Bezeichnung
   * @return name
  */
  @ApiModelProperty(example = "Raum 0815", required = true, value = "Bezeichnung")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationContext locationContext = (LocationContext) o;
    return Objects.equals(this.id, locationContext.id) &&
        Objects.equals(this.name, locationContext.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationContext {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

