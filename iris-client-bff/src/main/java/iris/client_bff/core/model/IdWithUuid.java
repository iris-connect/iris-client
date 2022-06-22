package iris.client_bff.core.model;

import java.util.UUID;

/**
 * @author Jens Kutzsche
 */
public abstract class IdWithUuid implements Id {

	private static final long serialVersionUID = 1932442646942290069L;

	protected abstract UUID getBasicId();

	@Override
	public String toString() {
		return getBasicId().toString();
	}

	public UUID toUuid() {
		return getBasicId();
	}
}
