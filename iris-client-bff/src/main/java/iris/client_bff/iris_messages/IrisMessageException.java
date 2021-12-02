package iris.client_bff.iris_messages;

import java.io.Serial;

public class IrisMessageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8068203942662884747L;

    public IrisMessageException(String failedMethod, Throwable cause) {
        super("Call to '" + failedMethod + "' failed", cause);
    }

    public IrisMessageException(Throwable cause) {
        super(cause);
    }
}
