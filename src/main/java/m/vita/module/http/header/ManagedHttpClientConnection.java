package m.vita.module.http.header;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSession;

public interface ManagedHttpClientConnection extends HttpClientConnection, HttpInetConnection {

    /**
     * Returns connection ID which is expected to be unique
     * for the life span of the connection manager.
     */
    String getId();

    /**
     * Binds this connection to the given socket. The connection
     * is considered open if it is bound and the underlying socket
     * is connection to a remote host.
     *
     * @param socket the socket to bind the connection to.
     * @throws IOException
     */
    void bind(Socket socket) throws IOException;

    /**
     * Returns the underlying socket.
     */
    Socket getSocket();

    /**
     * Obtains the SSL session of the underlying connection, if any.
     * If this connection is open, and the underlying socket is an
     * {@link javax.net.ssl.SSLSocket SSLSocket}, the SSL session of
     * that socket is obtained. This is a potentially blocking operation.
     *
     * @return  the underlying SSL session if available,
     *          <code>null</code> otherwise
     */
    SSLSession getSSLSession();

}
