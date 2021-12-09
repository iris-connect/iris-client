package iris.client_bff.status;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Jens Kutzsche
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AppStatusInternalException extends AppStatusException {

	private static final long serialVersionUID = -298204038489192894L;
	private Throwable exception;

	public AppStatusInternalException(String message, Throwable t) {
		super(message);

		this.exception = t;
	}
}
