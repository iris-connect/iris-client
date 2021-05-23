package iris.client_bff.events;

import static java.time.temporal.ChronoUnit.*;

import iris.client_bff.DataInitializer;
import iris.client_bff.core.Sex;
import iris.client_bff.events.model.Address;
import iris.client_bff.events.model.AttendanceInformation;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.model.Guest;
import iris.client_bff.events.model.GuestListDataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(20)
public class EventDataSubmissionsDataInitializer implements DataInitializer {

  private final EventDataSubmissionRepository submissions;

  /*
   * (non-Javadoc)
   * @see quarano.core.DataInitializer#initialize()
   */
  @Override
  public void initialize() {

	log.debug("Test data: creating data submissions …");

	var guests = new HashSet<Guest>();

	var address = new Address("Straße C", "1", "Stadt C", "12345");
	var dataProvider = new GuestListDataProvider("DIR", address);

	var submission = new EventDataSubmission(EventDataRequestsDataInitializer.DATA_REQUEST_1, guests, dataProvider,
		null,
		null,
		null);

	address = new Address("Straße A", "1", "Stadt A", "12345");
	var addInfos = new AttendanceInformation("kurzzeitiger Gast", Instant.now().minus(1, DAYS).minus(1, HOURS),
		Instant.now().minus(1, DAYS).plus(1, HOURS), "nichts zu sagen");
	var guest = new Guest(UUID.randomUUID(), submission, "Erster", "Gast",
		LocalDate.of(1990, 1, 1), Sex.UNKNOWN, "e@mail.de",
		"0815", "4711", Boolean.TRUE, address, addInfos);
	guests.add(guest);

	address = new Address("Straße B", "1", "Stadt B", "12345");
	addInfos = new AttendanceInformation("langer Gast", Instant.now().minus(1, DAYS).minus(1, HOURS),
		Instant.now().minus(1, DAYS).plus(1, HOURS), "nichts zu sagen");
	guest = new Guest(UUID.randomUUID(), submission, "Zweiter", "Gast",
		LocalDate.of(1980, 1, 1), Sex.UNKNOWN, "email@mail.de",
		"0815", "4711", Boolean.FALSE, address, addInfos);
	guests.add(guest);

	submissions.save(submission);
  }
}
