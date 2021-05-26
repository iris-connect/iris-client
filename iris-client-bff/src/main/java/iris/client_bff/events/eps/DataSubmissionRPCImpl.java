package iris.client_bff.events.eps;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import iris.client_bff.events.EventDataRequestService;
import iris.client_bff.events.EventDataSubmissionService;
import iris.client_bff.events.web.dto.GuestList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class DataSubmissionRPCImpl implements DataSubmissionRPC {

  private final EventDataRequestService requestService;
  private final EventDataSubmissionService dataSubmissionService;

  public String submitGuestList(JsonRpcClientDto client, UUID dataAuthorizationToken, GuestList guestList) {
    log.trace("Start submission {}", dataAuthorizationToken);

    // Todo check client.getName() vs. providerId

    return requestService.findById(dataAuthorizationToken).map(dataRequest -> {

      dataSubmissionService.save(dataRequest, guestList);

      log.trace("Done submission {}", dataAuthorizationToken);

      return "OK";

    }).orElseGet(() -> {

      // TODO sufficient?
      // probably throw exception

      log.error("Data submission for unknown data request occurred: {}", dataAuthorizationToken);

      return "Unknown requestId: " + dataAuthorizationToken.toString();
    });
  }
}
