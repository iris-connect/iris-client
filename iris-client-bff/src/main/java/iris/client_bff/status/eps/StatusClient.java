package iris.client_bff.status.eps;

import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;

import java.util.List;

public interface StatusClient {

    List<DirectoryEntry> getAvailableApps();

    Ping checkApp(String epsEndpoint);

}
