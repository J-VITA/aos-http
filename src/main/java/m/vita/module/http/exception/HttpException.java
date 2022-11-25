package m.vita.module.http.exception;

public class HttpException extends Exception {

    private static final long serialVersionUID = -5437299376222011036L;

    /**
     * Creates a new HttpException with a <tt>null</tt> detail message.
     */
    public HttpException() {
        super();
    }

    /**
     * Creates a new HttpException with the specified detail message.
     *
     * @param message the exception detail message
     */
    public HttpException(final String message) {
        super(message);
    }

    /**
     * Creates a new HttpException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public HttpException(final String message, final Throwable cause) {
        super(message);
        initCause(cause);
    }

}
