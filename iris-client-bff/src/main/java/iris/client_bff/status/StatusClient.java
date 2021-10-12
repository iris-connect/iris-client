package iris.client_bff.status;

import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;

import java.util.List;

public interface StatusClient {

    String getAppStati();

    List<DirectoryEntry> getAvailableApps();

    Ping queryEPSStatus(String epsEndpoint);
}
