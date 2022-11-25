package m.vita.module.http.exception;

import m.vita.module.http.annotation.Immutable;

@Immutable
public class RedirectException extends ProtocolException {

    /**
     * Creates a new RedirectException with a <tt>null</tt> detail message.
     */
    public RedirectException() {
        super();
    }

    /**
     * Creates a new RedirectException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public RedirectException(final String message) {
        super(message);
    }

    /**
     * Creates a new RedirectException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public RedirectException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
