package iris.client_bff.status;

import iris.client_bff.status.eps.EPSStatusClient;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatusService {

	private final EPSStatusClient statusClient;
	private final AppStatus.Resolver statusResolver;

	public Apps getApps() {
		return statusClient.getAvailableApps().sort();
	}

	public AppInfo getAppInfo(String appName) {

		try {

			var appInfo = statusClient.checkApp(appName);

			return appInfo.setStatus(this.statusResolver.getStatusOk());

		} catch (AppStatusException e) {

			var status = e.isLocal()
					? this.statusResolver.getCantConnectLocalEps()
					: this.statusResolver.getStatusByErrorMessage(e.getMessage());

			return new AppInfo(appName, "").setStatus(status);
		}
	}
}
