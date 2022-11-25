package m.vita.module.http.exception;

import java.io.IOException;

public class NoHttpResponseException extends IOException {

    private static final long serialVersionUID = -7658940387386078766L;

    /**
     * Creates a new NoHttpResponseException with the specified detail message.
     *
     * @param message exception message
     */
    public NoHttpResponseException(final String message) {
        super(message);
    }

}
