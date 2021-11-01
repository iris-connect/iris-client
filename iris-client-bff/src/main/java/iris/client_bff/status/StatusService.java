package iris.client_bff.status;

import iris.client_bff.status.eps.StatusClient;
import iris.client_bff.status.web.AppStatusResolver;
import iris.client_bff.status.web.dto.AppStatus;
import iris.client_bff.status.web.dto.AppStatusInfo;
import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatusService {

    private final StatusClient statusClient;
    private final AppStatusResolver statusResolver;

    public List<DirectoryEntry> getApps() {
        List <DirectoryEntry> entryList = statusClient.getAvailableApps();
        entryList.sort(Comparator.comparing(DirectoryEntry::getName, String.CASE_INSENSITIVE_ORDER));
        return entryList;
    }

    public AppStatusInfo getAppStatusInfo(String appName) {
        try {
            Ping ping = statusClient.checkApp(appName);
            AppStatus status = this.statusResolver.getStatusOk();
            return AppStatusInfo.builder()
                    .info(ping)
                    .status(status.getStatus())
                    .message(status.getMessage())
                    .build();
        } catch (Throwable t) {
            AppStatus status = this.statusResolver.getStatusByErrorMessage(t.getMessage());
            return AppStatusInfo.builder()
                    .status(status.getStatus())
                    .message(status.getMessage())
                    .build();
        }
    }

}
