package iris.client_bff.cases;

import iris.client_bff.DataInitializer;
import iris.client_bff.cases.model.CaseDataProvider;
import iris.client_bff.cases.model.CaseDataSubmission;
import iris.client_bff.cases.model.CaseEvent;
import iris.client_bff.cases.model.Contact;
import iris.client_bff.core.Sex;
import iris.client_bff.core.model.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(20)
public class CaseDataSubmissionsDataInitializer implements DataInitializer {

	private final CaseDataSubmissionRepository submissions;

	@Override
	public void initialize() {

		log.debug("Test data: creating data submissions …");

		var contacts = new HashSet<Contact>();
		var events = new HashSet<CaseEvent>();

		var address = new Address("Straße C", "1", "Stadt C", "12345");
		var dataProvider = new CaseDataProvider("FN", "LN", LocalDate.now());

		var submission = new CaseDataSubmission(CaseDataRequestDataInitializer.DATA_REQUEST_1, contacts, null,
				null, events, null, null, dataProvider);

		address = new Address("Straße A", "1", "Stadt A", "12345");
		var contact = new Contact(submission, "Erster", "Gast",
				LocalDate.of(1990, 1, 1), Sex.UNKNOWN, "e@mail.de",
				"0815", "4711", address, null, null, null, null, null, null, null, null);
		contacts.add(contact);

		address = new Address("Straße B", "1", "Stadt B", "12345");
		contact = new Contact(submission, "Zweiter", "Gast",
				LocalDate.of(1980, 1, 1), Sex.UNKNOWN, "email@mail.de",
				"0815", "4711", address, null, null, null, null, null, null, null, null);
		contacts.add(contact);

		events.add(new CaseEvent(submission, "Event", null, null, null));

		submissions.save(submission);
	}
}
