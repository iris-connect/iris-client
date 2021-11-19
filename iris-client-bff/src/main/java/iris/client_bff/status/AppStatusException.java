package iris.client_bff.status;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Jens Kutzsche
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AppStatusException extends RuntimeException {

	private static final long serialVersionUID = -7919796135106115932L;

	boolean local;

	public AppStatusException(String message, boolean local) {
		super(message);
		this.local = local;
	}
}
