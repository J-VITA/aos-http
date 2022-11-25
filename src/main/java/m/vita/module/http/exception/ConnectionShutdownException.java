package m.vita.module.http.exception;

import m.vita.module.http.annotation.Immutable;

@Immutable
public class ConnectionShutdownException extends IllegalStateException {

    private static final long serialVersionUID = 5868657401162844497L;

    /**
     * Creates a new ConnectionShutdownException with a <tt>null</tt> detail message.
     */
    public ConnectionShutdownException() {
        super();
    }

}