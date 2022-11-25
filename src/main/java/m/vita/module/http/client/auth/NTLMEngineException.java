package m.vita.module.http.client.auth;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.AuthenticationException;

@Immutable
public class NTLMEngineException extends AuthenticationException {

    public NTLMEngineException() {
        super();
    }

    /**
     * Creates a new NTLMEngineException with the specified message.
     *
     * @param message the exception detail message
     */
    public NTLMEngineException(final String message) {
        super(message);
    }

    /**
     * Creates a new NTLMEngineException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public NTLMEngineException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
