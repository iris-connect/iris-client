package iris.client_bff.status.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.status.AppStatus;
import iris.client_bff.status.AppStatus.Status;
import iris.client_bff.status.Apps.App;
import iris.client_bff.status.StatusService;
import iris.client_bff.status.web.AppStatusController.AppStatusInfo.Info;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonIgnore;

@RestController
@AllArgsConstructor
@RequestMapping("/status/checkin-apps")
public class AppStatusController {

	private static final String APP_NAME = "name";

	private StatusService statusService;
	private final ValidationHelper validationHelper;

	@GetMapping()
	public List<App> getApps() {

		try {
			return statusService.getApps().toList();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/{appName}")
	public ResponseEntity<AppStatusInfo> getAppStatusInfo(@PathVariable String appName) {

		validateAppName(appName);

		var appInfo = statusService.getAppInfo(appName);
		var dto = new AppStatusInfo(new Info(appInfo.getName(), appInfo.getVersion()), appInfo.getStatus());

		return ResponseEntity.ok(dto);
	}

	private void validateAppName(String appName) {

		if (validationHelper.isPossibleAttack(appName, APP_NAME, false)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
	}

	@Value
	class AppStatusInfo {

		private Info info;

		@JsonIgnore
		private AppStatus appStatus;

		public Status getStatus() {
			return appStatus.getStatus();
		}

		public MessageSourceResolvable getMessage() {
			return appStatus;
		}

		record Info(String name, String version) {}
	}
}
