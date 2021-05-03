/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.client.data_submission;

import static java.time.temporal.ChronoUnit.*;

import de.healthIMIS.iris.client.DataInitializer;
import de.healthIMIS.iris.client.data_request.DataRequestDataInitializer;
import de.healthIMIS.iris.client.data_submission.model.Address;
import de.healthIMIS.iris.client.data_submission.model.AttendanceInformation;
import de.healthIMIS.iris.client.data_submission.model.DataSubmission;
import de.healthIMIS.iris.client.data_submission.model.Guest;
import de.healthIMIS.iris.client.data_submission.model.GuestListDataProvider;
import de.healthIMIS.iris.client.data_submission.model.Guest.Sex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class DataSubmissionDataInitializer implements DataInitializer {

	private final DataSubmissionRepository submissions;

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

		var submission = new DataSubmission(DataRequestDataInitializer.DATA_REQUEST_1, guests, dataProvider, null, null,
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
