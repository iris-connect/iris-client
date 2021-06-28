package iris.client_bff.events.model;

import iris.client_bff.core.Sex;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Guest {

  @Id
  private UUID guestId = UUID.randomUUID();

  @ManyToOne //
  @JoinColumn(name = "submission_id")
  private EventDataSubmission submission;

  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;

  @Enumerated(EnumType.STRING)
  private Sex sex = Sex.UNKNOWN;

  private String email;
  private String phone;
  private String mobilePhone;
  private Boolean identityChecked;

  @Embedded
  private Address address;

  @Embedded
  private AttendanceInformation attendanceInformation;
}
