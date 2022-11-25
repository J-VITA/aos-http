package m.vita.module.http.exception;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Arrays;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.header.HttpHost;

@Immutable
public class HttpHostConnectException extends ConnectException {

    private final HttpHost host;

    /**
     * @deprecated (4.3) use {@link #HttpHostConnectException(IOException, HttpHost,
     *   InetAddress...)}
     */
    @Deprecated
    public HttpHostConnectException(final HttpHost host, final ConnectException cause) {
        this(cause, host, (InetAddress) null);
    }

    /**
     * Creates a HttpHostConnectException based on orRequestParamsiginal {@link IOException}.
     *
     * @since 4.3
     */
    public HttpHostConnectException(
            final IOException cause,
            final HttpHost host,
            final InetAddress... remoteAddresses) {
        super("Connect to " +
                (host != null ? host.toHostString() : "remote host") +
                (remoteAddresses != null && remoteAddresses .length > 0 ?
                        " " + Arrays.asList(remoteAddresses) : "") +
                ((cause != null && cause.getMessage() != null) ?
                        " failed: " + cause.getMessage() : " refused"));
        this.host = host;
        initCause(cause);
    }

    public HttpHost getHost() {
        return this.host;
    }

}
