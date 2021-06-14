package iris.client_bff.cases;

import static java.time.temporal.ChronoUnit.*;

import iris.client_bff.DataInitializer;
import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaseDataRequestDataInitializer implements DataInitializer {

  public static final DataRequestIdentifier REQ_ID_1 = DataRequestIdentifier
	  .of(UUID.fromString("ad12261b-85c2-48e5-bce1-c60b0eb8040a"));
  static final DataRequestIdentifier REQ_ID_2 = DataRequestIdentifier
	  .of(UUID.fromString("d191842e-f80d-420e-9165-fe5585ae1731"));
  static final DataRequestIdentifier REQ_ID_3 = DataRequestIdentifier
	  .of(UUID.fromString("7bf1eb00-3d84-45c7-8b08-f0e4719ee762"));

  public static final CaseDataRequest DATA_REQUEST_1 = new CaseDataRequest(REQ_ID_1.toString(), "Anfrage 1",
	  Instant.now().minus(2, DAYS), null, null, null);

  private final CaseDataRequestRepository requests;

  /*
   * (non-Javadoc)
   * @see quarano.core.DataInitializer#initialize()
   */
  @Override
  public void initialize() {

	log.debug("Test data: creating data requests …");

	var list = new ArrayList<CaseDataRequest>();

	list.add(DATA_REQUEST_1);

	list.add(new CaseDataRequest(REQ_ID_2.toString(), "Anfrage 2", Instant.now().minus(4, DAYS),
		Instant.now().minus(2, DAYS), null, null));

	list.add(new CaseDataRequest(REQ_ID_3.toString(), "Anfrage 3", Instant.now().minus(4, DAYS),
		Instant.now().minus(2, DAYS), null, null));

	requests.saveAll(list);
  }
}
