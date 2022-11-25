package m.vita.module.http.exception;

import m.vita.module.http.annotation.Immutable;

@Immutable
public class MalformedChallengeException extends ProtocolException {

    private static final long serialVersionUID = 814586927989932284L;

    /**
     * Creates a new MalformedChallengeException with a <tt>null</tt> detail message.
     */
    public MalformedChallengeException() {
        super();
    }

    /**
     * Creates a new MalformedChallengeException with the specified message.
     *
     * @param message the exception detail message
     */
    public MalformedChallengeException(final String message) {
        super(message);
    }

    /**
     * Creates a new MalformedChallengeException with the specified detail message and cause.
     *
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public MalformedChallengeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
