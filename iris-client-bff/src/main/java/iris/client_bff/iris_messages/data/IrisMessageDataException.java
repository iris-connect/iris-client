package iris.client_bff.iris_messages.data;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.Serial;

public class IrisMessageDataException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8707865003747114093L;

    public IrisMessageDataException(String failedMethod, Throwable cause) {
        super("Call to '" + failedMethod + "' failed", cause);
    }

    public IrisMessageDataException(String message) {
        super(message);
    }

    public IrisMessageDataException(Throwable cause) {
        super(cause);
    }

    public String getErrorMessage() {
        return ExceptionUtils.getRootCause(this).getMessage();
    }
}
