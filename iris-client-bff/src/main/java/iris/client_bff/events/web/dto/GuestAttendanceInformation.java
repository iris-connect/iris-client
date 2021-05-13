package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class GuestAttendanceInformation {

  private String descriptionOfParticipation;
  private Instant attendFrom;
  private Instant attendTo;
  private String additionalInformation;
}
