package iris.client_bff.cases.eps;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import iris.client_bff.cases.eps.dto.Contacts;
import iris.client_bff.cases.eps.dto.DataProvider;
import iris.client_bff.cases.eps.dto.Events;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class CaseDataControllerImpl implements CaseDataController {

  @Override
  public String submitContactAndEventData(UUID dataAuthorizationToken, @Valid Contacts contacts, @Valid Events events,
      @Valid DataProvider dataProvider) {
    System.out.println("dataAuthorizationToken = " + dataAuthorizationToken);
    System.out.println("contacts = " + contacts);
    System.out.println("events = " + events);
    System.out.println("dataProvider = " + dataProvider);
    return null;
  }
}
