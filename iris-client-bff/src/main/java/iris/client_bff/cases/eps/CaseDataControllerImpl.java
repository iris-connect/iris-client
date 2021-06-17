package iris.client_bff.cases.eps;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
public class CaseDataControllerImpl implements CaseDataController {

  @Override
  public String another(String name) {
    System.out.println("true = " + true);
    return "ASD" + name;
  }
}
