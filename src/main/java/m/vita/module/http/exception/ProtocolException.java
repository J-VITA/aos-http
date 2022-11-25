package m.vita.module.http.exception;

public class ProtocolException extends HttpException {

    private static final long serialVersionUID = -2143571074341228994L;

    /**
     * Creates a new ProtocolException with a <tt>null</tt> detail message.
     */
    public ProtocolException() {
        super();
    }

    /**
     * Creates a new ProtocolException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public ProtocolException(final String message) {
        super(message);
    }

    /**
     * Creates a new ProtocolException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public ProtocolException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
