package m.vita.module.http.exception;

import java.io.IOException;

public class MessageConstraintException extends IOException {

    private static final long serialVersionUID = 6077207720446368695L;

    /**
     * Creates a TruncatedChunkException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public MessageConstraintException(final String message) {
        super(message);
    }

}
