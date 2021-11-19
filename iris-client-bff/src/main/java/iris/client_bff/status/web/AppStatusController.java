package iris.client_bff.status.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.status.StatusService;
import iris.client_bff.status.eps.dto.DirectoryEntry;
import iris.client_bff.status.web.dto.AppStatusInfo;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/status/checkin-apps")
public class AppStatusController {

	private static final String APP_NAME = "name";

	private StatusService statusService;
	private final ValidationHelper validationHelper;

	@GetMapping()
	public List<DirectoryEntry> getApps() {

		try {
			return statusService.getApps();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/{appName}")
	public ResponseEntity<AppStatusInfo> getAppStatusInfo(@PathVariable String appName) {

		validateAppName(appName);

		return ResponseEntity.ok(statusService.getAppStatusInfo(appName));
	}

	private void validateAppName(String appName) {

		if (validationHelper.isPossibleAttack(appName, APP_NAME, false)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
	}
}
