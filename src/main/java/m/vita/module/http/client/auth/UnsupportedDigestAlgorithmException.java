package m.vita.module.http.client.auth;

import m.vita.module.http.annotation.Immutable;

@Immutable
public class UnsupportedDigestAlgorithmException extends RuntimeException {


    /**
     * Creates a new UnsupportedAuthAlgoritmException with a <tt>null</tt> detail message.
     */
    public UnsupportedDigestAlgorithmException() {
        super();
    }

    /**
     * Creates a new UnsupportedAuthAlgoritmException with the specified message.
     *
     * @param message the exception detail message
     */
    public UnsupportedDigestAlgorithmException(final String message) {
        super(message);
    }

    /**
     * Creates a new UnsupportedAuthAlgoritmException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public UnsupportedDigestAlgorithmException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
