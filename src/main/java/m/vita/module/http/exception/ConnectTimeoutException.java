package m.vita.module.http.exception;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.util.Arrays;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.header.HttpHost;

@Immutable
public class ConnectTimeoutException extends InterruptedIOException {

    private static final long serialVersionUID = -4816682903149535989L;

    private final HttpHost host;

    /**
     * Creates a ConnectTimeoutException with a <tt>null</tt> detail message.
     */
    public ConnectTimeoutException() {
        super();
        this.host = null;
    }

    /**
     * Creates a ConnectTimeoutException with the specified detail message.
     */
    public ConnectTimeoutException(final String message) {
        super(message);
        this.host = null;
    }

    /**
     * Creates a ConnectTimeoutException based on original {@link IOException}.
     *
     * @since 4.3
     */
    public ConnectTimeoutException(
            final IOException cause,
            final HttpHost host,
            final InetAddress... remoteAddresses) {
        super("Connect to " +
                (host != null ? host.toHostString() : "remote host") +
                (remoteAddresses != null && remoteAddresses.length > 0 ?
                        " " + Arrays.asList(remoteAddresses) : "") +
                ((cause != null && cause.getMessage() != null) ?
                        " failed: " + cause.getMessage() : " timed out"));
        this.host = host;
        initCause(cause);
    }

    /**
     * @since 4.3
     */
    public HttpHost getHost() {
        return host;
    }

}
