package de.healthIMIS.iris.client.data_request.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

  private Instant start;

  private Instant end;

  private Instant requestedAt;

  private Instant lastModifiedAt;

  private String requestDetails;

  private LocationInformation locationInformation;

  private GuestList submissionData;

}

