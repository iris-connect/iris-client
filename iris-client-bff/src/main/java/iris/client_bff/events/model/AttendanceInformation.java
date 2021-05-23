package iris.client_bff.events.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

import javax.persistence.Embeddable;

@Embeddable
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class AttendanceInformation {

  private String descriptionOfParticipation;
  private Instant attendFrom;
  private Instant attendTo;
  private String additionalInformation;
}
