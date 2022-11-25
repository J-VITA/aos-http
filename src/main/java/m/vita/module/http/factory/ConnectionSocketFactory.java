package m.vita.module.http.factory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;

public interface ConnectionSocketFactory {

    /**
     * Creates new, unconnected socket. The socket should subsequently be passed to
     * {@link #connectSocket(int, Socket, HttpHost, InetSocketAddress, InetSocketAddress,
     *    HttpContext) connectSocket} method.
     *
     * @return  a new socket
     *
     * @throws IOException if an I/O error occurs while creating the socket
     */
    Socket createSocket(HttpContext context) throws IOException;

    /**
     * Connects the socket to the target host with the given resolved remote address.
     *
     * @param connectTimeout connect timeout.
     * @param sock the socket to connect, as obtained from {@link #createSocket(HttpContext)}.
     * <code>null</code> indicates that a new socket should be created and connected.
     * @param host target host as specified by the caller (end user).
     * @param remoteAddress the resolved remote address to connect to.
     * @param localAddress the local address to bind the socket to, or <code>null</code> for any.
     * @param context the actual HTTP context.
     *
     * @return  the connected socket. The returned object may be different
     *          from the <code>sock</code> argument if this factory supports
     *          a layered protocol.
     *
     * @throws IOException if an I/O error occurs
     */
    Socket connectSocket(
            int connectTimeout,
            Socket sock,
            HttpHost host,
            InetSocketAddress remoteAddress,
            InetSocketAddress localAddress,
            HttpContext context) throws IOException;

}
