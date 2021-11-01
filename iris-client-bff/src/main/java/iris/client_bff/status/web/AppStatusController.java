package iris.client_bff.status.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.status.StatusService;
import iris.client_bff.status.web.dto.AppStatusInfo;
import iris.client_bff.status.eps.dto.DirectoryEntry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/status/checkin-apps")
public class AppStatusController {

	private static final String APP_NAME = "name";

	private StatusService statusService;
	private final ValidationHelper validationHelper;

	@GetMapping()
	@ResponseStatus(OK)
	public List<DirectoryEntry> getApps() {
		try {
			return statusService.getApps();
		} catch (Throwable e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/{appName}")
	@ResponseStatus(OK)
	public ResponseEntity<AppStatusInfo> getAppStatusInfo(@PathVariable String appName) {
		if (this.isAppNameValid(appName)) {
			try {
				return ResponseEntity.ok(statusService.getAppStatusInfo(appName));
			} catch (Throwable e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	private boolean isAppNameValid(String appName) {
		return (!validationHelper.isPossibleAttack(appName, APP_NAME, false));
	}

}
