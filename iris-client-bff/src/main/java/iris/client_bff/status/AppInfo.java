package iris.client_bff.status;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jens Kutzsche
 */
@Data
@RequiredArgsConstructor
public class AppInfo {

	private final String name;
	private final String version;
	private AppStatus status;

	boolean isTooOld() {
		return StringUtils.isBlank(version);
	}
}
