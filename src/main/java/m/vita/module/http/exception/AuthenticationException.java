package m.vita.module.http.exception;


import m.vita.module.http.annotation.Immutable;

@Immutable
public class AuthenticationException extends ProtocolException {

    /**
     * Creates a new AuthenticationException with a <tt>null</tt> detail message.
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Creates a new AuthenticationException with the specified message.
     *
     * @param message the exception detail message
     */
    public AuthenticationException(final String message) {
        super(message);
    }

    /**
     * Creates a new AuthenticationException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
