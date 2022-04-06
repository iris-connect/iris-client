package iris.client_bff.status;

import iris.client_bff.status.eps.EPSStatusClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StatusService {

	private final EPSStatusClient statusClient;
	private final AppStatus.Resolver statusResolver;

	public Apps getApps() {
		return statusClient.getAvailableApps().sort();
	}

	public AppInfo getAppInfo(String appName) {

		try {

			var appInfo = statusClient.checkApp(appName);

			return statusResolver.resolveAndSetStatus(appInfo);

		} catch (AppStatusException e) {

			AppStatus status = statusResolver.getStatusByException(e);

			if (e instanceof EpsConnectionException) {
				log.error("Can't determine app status: Can't connect client EPS: {}", e.getMessage());
			} else if (e instanceof AppStatusInternalException) {
				log.error("Can't determine app status: Internal exception: ", e);
			} else if (status == AppStatus.UNKNOWN_ERROR) {
				log.info("CheckApp for {} ends with an unknown error; the origin message was: {}", appName, e.getMessage());
			}

			return new AppInfo(appName, "").setStatus(status);
		}
	}
}
