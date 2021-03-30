package de.healthIMIS.iris.client.data_request.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class DataRequestDetails   {

  public enum StatusEnum {
    DATA_REQUESTED("DATA_REQUESTED"),
    
    DATA_RECEIVED("DATA_RECEIVED"),
    
    CLOSED("CLOSED");

    private String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private String code;

  private String name;

  private String externalRequestId;

  private ZonedDateTime start;

  private ZonedDateTime end;

  private String requestDetails;

  private LocationInformation locationInformation;

  @Valid
  private List<Guest> guests = new ArrayList<>();

  private GuestListDataProvider dataProvider;

  private String additionalInformation;

  private ZonedDateTime startDate;

  private ZonedDateTime endDate;

}

