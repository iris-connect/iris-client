package iris.client_bff.proxy;

import java.util.UUID;

public interface ProxyServiceClient {
  String announce() throws IRISAnnouncementException;
}
