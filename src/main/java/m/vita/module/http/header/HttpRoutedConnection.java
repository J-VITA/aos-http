package m.vita.module.http.header;

import javax.net.ssl.SSLSession;

public interface HttpRoutedConnection extends HttpInetConnection {

    /**
     * Indicates whether this connection is secure.
     * The return value is well-defined only while the connection is open.
     * It may change even while the connection is open.
     *
     * @return  <code>true</code> if this connection is secure,
     *          <code>false</code> otherwise
     */
    boolean isSecure();

    /**
     * Obtains the current route of this connection.
     *
     * @return  the route established so far, or
     *          <code>null</code> if not connected
     */
    HttpRoute getRoute();

    /**
     * Obtains the SSL session of the underlying connection, if any.
     * If this connection is open, and the underlying socket is an
     * {@link javax.net.ssl.SSLSocket SSLSocket}, the SSL session of
     * that socket is obtained. This is a potentially blocking operation.
     * <br/>
     * <b>Note:</b> Whether the underlying socket is an SSL socket
     * can not necessarily be determined via {@link #isSecure}.
     * Plain sockets may be considered secure, for example if they are
     * connected to a known host in the same network segment.
     * On the other hand, SSL sockets may be considered insecure,
     * for example depending on the chosen cipher suite.
     *
     * @return  the underlying SSL session if available,
     *          <code>null</code> otherwise
     */
    SSLSession getSSLSession();

}
