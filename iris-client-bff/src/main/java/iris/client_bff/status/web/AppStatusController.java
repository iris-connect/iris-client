package iris.client_bff.status.web;

import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.status.AppStatus;
import iris.client_bff.status.AppStatus.Status;
import iris.client_bff.status.Apps.App;
import iris.client_bff.status.StatusService;
import iris.client_bff.status.web.AppStatusController.AppStatusInfo.Info;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonIgnore;

@RestController
@RequestMapping("/status/checkin-apps")
@Validated
@AllArgsConstructor
@Slf4j
public class AppStatusController {

	private StatusService statusService;

	@GetMapping()
	public List<App> getApps() {

		try {
			return statusService.getApps().toList();
		} catch (Exception e) {

			log.error("Can't get apps from StatusService: ", e);

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/{appName}")
	public ResponseEntity<AppStatusInfo> getAppStatusInfo(
			@PathVariable @NoSignOfAttack(obfuscateLogging = false) String appName) {

		var appInfo = statusService.getAppInfo(appName);
		var dto = new AppStatusInfo(new Info(appInfo.getName(), appInfo.getVersion()), appInfo.getStatus());

		return ResponseEntity.ok(dto);
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
