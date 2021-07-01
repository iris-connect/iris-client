package iris.client_bff.events;

import static java.time.temporal.ChronoUnit.*;

import iris.client_bff.DataInitializer;
import iris.client_bff.events.EventDataRequest.DataRequestIdentifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(10)
public class EventDataRequestsDataInitializer implements DataInitializer {

	public static final DataRequestIdentifier REQ_ID_1 = DataRequestIdentifier
			.of(UUID.fromString("790b9a69-17f8-4ba7-a8ae-2f7bf34e0b80"));
	static final DataRequestIdentifier REQ_ID_2 = DataRequestIdentifier
			.of(UUID.fromString("2707fd28-9b4f-4140-b80e-d56d9aad831f"));
	static final DataRequestIdentifier REQ_ID_3 = DataRequestIdentifier
			.of(UUID.fromString("3907e730-af89-4944-8e75-fbe6ba60c904"));

	public static final EventDataRequest DATA_REQUEST_1 = new EventDataRequest(REQ_ID_1.toString(), "Anfrage 1",
			Instant.now().minus(2, DAYS), null, null, null, null, null, null);

	private final EventDataRequestRepository requests;

	@Override
	public void initialize() {

		log.debug("Test data: creating data requests …");

		var list = new ArrayList<EventDataRequest>();

		list.add(DATA_REQUEST_1);

		list.add(new EventDataRequest(REQ_ID_2.toString(), "Anfrage 2", Instant.now().minus(4, DAYS),
				Instant.now().minus(2, DAYS), null, null, null, null, null));

		list.add(new EventDataRequest(REQ_ID_3.toString(), "Anfrage 3", Instant.now().minus(4, DAYS),
				Instant.now().minus(2, DAYS), null, null, null, null, null));

		requests.saveAll(list);
	}
}
