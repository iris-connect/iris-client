package iris.client_bff.status;

import iris.client_bff.status.eps.EPSStatusClient;
import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.eps.dto.Ping;
import iris.client_bff.status.web.dto.AppStatusInfo;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatusService {

	private final EPSStatusClient statusClient;
	private final AppStatus.Resolver statusResolver;

	public List<DirectoryEntry> getApps() {

		List<DirectoryEntry> entryList = statusClient.getAvailableApps();

		entryList.sort(Comparator.comparing(DirectoryEntry::getName, String.CASE_INSENSITIVE_ORDER));

		return entryList;
	}

	public AppStatusInfo getAppStatusInfo(String appName) {

		try {

			Ping ping = statusClient.checkApp(appName);
			AppStatus status = this.statusResolver.getStatusOk();

			return AppStatusInfo.of(status).withInfo(ping);

		} catch (AppStatusException e) {

			if (e.isLocal()) {
				return AppStatusInfo.of(this.statusResolver.getCantConnectLocalEps());
			}

			return AppStatusInfo.of(this.statusResolver.getStatusByErrorMessage(e.getMessage()));
		}
	}
}
