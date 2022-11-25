package m.vita.module.http.factory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.connect.HttpConnectionParams;
import m.vita.module.http.exception.ConnectTimeoutException;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.resolve.HostNameResolver;
import m.vita.module.http.util.Args;

@Immutable
public class PlainSocketFactory implements SocketFactory, SchemeSocketFactory {

    private final HostNameResolver nameResolver;

    /**
     * Gets the default factory.
     *
     * @return the default factory
     */
    public static PlainSocketFactory getSocketFactory() {
        return new PlainSocketFactory();
    }

    /**
     * @deprecated (4.1) use {@link cz.msebera.android.httpclient.conn.DnsResolver}
     */
    @Deprecated
    public PlainSocketFactory(final HostNameResolver nameResolver) {
        super();
        this.nameResolver = nameResolver;
    }

    public PlainSocketFactory() {
        super();
        this.nameResolver = null;
    }

    /**
     * @param params Optional parameters. Parameters passed to this method will have no effect.
     *               This method will create a unconnected instance of {@link Socket} class
     *               using default constructor.
     *
     * @since 4.1
     */
    public Socket createSocket(final HttpParams params) {
        return new Socket();
    }

    public Socket createSocket() {
        return new Socket();
    }

    /**
     * @since 4.1
     */
    public Socket connectSocket(
            final Socket socket,
            final InetSocketAddress remoteAddress,
            final InetSocketAddress localAddress,
            final HttpParams params) throws IOException, ConnectTimeoutException {
        Args.notNull(remoteAddress, "Remote address");
        Args.notNull(params, "HTTP parameters");
        Socket sock = socket;
        if (sock == null) {
            sock = createSocket();
        }
        if (localAddress != null) {
            sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
            sock.bind(localAddress);
        }
        final int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        final int soTimeout = HttpConnectionParams.getSoTimeout(params);

        try {
            sock.setSoTimeout(soTimeout);
            sock.connect(remoteAddress, connTimeout);
        } catch (final SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }
        return sock;
    }

    /**
     * Checks whether a socket connection is secure.
     * This factory creates plain socket connections
     * which are not considered secure.
     *
     * @param sock      the connected socket
     *
     * @return  <code>false</code>
     */
    public final boolean isSecure(final Socket sock) {
        return false;
    }

    /**
     * @deprecated (4.1)  Use {@link #connectSocket(Socket, InetSocketAddress, InetSocketAddress, HttpParams)}
     */
    @Deprecated
    public Socket connectSocket(
            final Socket socket,
            final String host, final int port,
            final InetAddress localAddress, final int localPort,
            final HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        InetSocketAddress local = null;
        if (localAddress != null || localPort > 0) {
            local = new InetSocketAddress(localAddress, localPort > 0 ? localPort : 0);
        }
        final InetAddress remoteAddress;
        if (this.nameResolver != null) {
            remoteAddress = this.nameResolver.resolve(host);
        } else {
            remoteAddress = InetAddress.getByName(host);
        }
        final InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
        return connectSocket(socket, remote, local, params);
    }

}
