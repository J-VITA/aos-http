package m.vita.module.http.exception;

import java.io.IOException;

public class ConnectionClosedException extends IOException {

    /**
     * Creates a new ConnectionClosedException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public ConnectionClosedException(final String message) {
        super(message);
    }

}
