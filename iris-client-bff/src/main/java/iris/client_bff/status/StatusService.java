package iris.client_bff.status;

import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatusService {

    private final StatusClient statusClient;

    public String getStati() {

        List<DirectoryEntry> apps = statusClient.getAvailableApps();

        for (DirectoryEntry app : apps) {
            log.debug(app.getName());
        }

        return statusClient.getAppStati();
    }

    public Ping getEPSStatus(String epsEndpoint) {
        return statusClient.queryEPSStatus(epsEndpoint);
    }

}
