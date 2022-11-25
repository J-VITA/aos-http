package m.vita.module.http.exception;

public class ParseException extends RuntimeException {

    private static final long serialVersionUID = -7288819855864183578L;

    /**
     * Creates a {@link ParseException} without details.
     */
    public ParseException() {
        super();
    }

    /**
     * Creates a {@link ParseException} with a detail message.
     *
     * @param message the exception detail message, or <code>null</code>
     */
    public ParseException(final String message) {
        super(message);
    }

}
