package iris.client_bff.cases;

import static java.time.temporal.ChronoUnit.*;

import iris.client_bff.DataInitializer;
import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.core.token.IdentifierToken;
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
public class CaseDataRequestDataInitializer implements DataInitializer {

	public static final DataRequestIdentifier REQ_ID_1 = DataRequestIdentifier
			.of(UUID.fromString("ad12261b-85c2-48e5-bce1-c60b0eb8040a"));
	public static final DataRequestIdentifier REQ_ID_2 = DataRequestIdentifier
			.of(UUID.fromString("d191842e-f80d-420e-9165-fe5585ae1731"));
	public static final DataRequestIdentifier REQ_ID_3 = DataRequestIdentifier
			.of(UUID.fromString("7bf1eb00-3d84-45c7-8b08-f0e4719ee762"));

	private static final IdentifierToken TOKEN_1 = IdentifierToken.builder().readableToken("readableToken1")
			.connectionAuthorizationToken("CAT1").dataAuthorizationToken("DAT1").build();
	private static final IdentifierToken TOKEN_2 = IdentifierToken.builder().readableToken("readableToken2")
			.connectionAuthorizationToken("CAT2").dataAuthorizationToken("DAT2").build();
	private static final IdentifierToken TOKEN_3 = IdentifierToken.builder().readableToken("readableToken3")
			.connectionAuthorizationToken("CAT3").dataAuthorizationToken("DAT3").build();

	public static final CaseDataRequest DATA_REQUEST_1 = new CaseDataRequest(REQ_ID_1.toString(), "Anfrage 1",
			Instant.now().minus(2, DAYS), null, null, null, TOKEN_1, null);

	private final CaseDataRequestRepository requests;

	@Override
	public void initialize() {

		log.debug("Test data: creating data requests …");

		var list = new ArrayList<CaseDataRequest>();

		list.add(DATA_REQUEST_1);

		list.add(new CaseDataRequest(REQ_ID_2.toString(), "Anfrage 2", Instant.now().minus(4, DAYS),
				Instant.now().minus(2, DAYS), null, null, TOKEN_2, null));

		list.add(new CaseDataRequest(REQ_ID_3.toString(), "Anfrage 3", Instant.now().minus(4, DAYS),
				Instant.now().minus(2, DAYS), null, null, TOKEN_3, null));

		requests.saveAll(list);
	}
}
