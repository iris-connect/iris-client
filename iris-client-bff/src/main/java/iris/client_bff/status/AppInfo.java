package iris.client_bff.status;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Jens Kutzsche
 */
@Data
@RequiredArgsConstructor
public class AppInfo {

	private final String name;
	private final String version;
	private AppStatus status;
}
