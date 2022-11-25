package m.vita.module.http.exception;

import java.io.IOException;

public class MalformedChunkCodingException extends IOException {

    private static final long serialVersionUID = 2158560246948994524L;

    /**
     * Creates a MalformedChunkCodingException without a detail message.
     */
    public MalformedChunkCodingException() {
        super();
    }

    /**
     * Creates a MalformedChunkCodingException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public MalformedChunkCodingException(final String message) {
        super(message);
    }

}
