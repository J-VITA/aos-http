package m.vita.module.http.factory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import m.vita.module.http.exception.ConnectTimeoutException;
import m.vita.module.http.header.HttpParams;

public interface SchemeSocketFactory {

    /**
     * Creates a new, unconnected socket. The socket should subsequently be passed to
     * {@link #connectSocket(Socket, InetSocketAddress, InetSocketAddress, HttpParams)}.
     *
     * @param params    Optional {@link HttpParams parameters}. In most cases these parameters
     *                  will not be required and will have no effect, as usually socket
     *                  initialization should take place in the
     *                  {@link #connectSocket(Socket, InetSocketAddress, InetSocketAddress, HttpParams)}
     *                  method. However, in rare cases one may want to pass additional parameters
     *                  to this method in order to create a customized {@link Socket} instance,
     *                  for instance bound to a SOCKS proxy server.
     *
     * @return  a new socket
     *
     * @throws IOException if an I/O error occurs while creating the socket
     */
    Socket createSocket(HttpParams params) throws IOException;

    /**
     * Connects a socket to the target host with the given remote address.
     * <p/>
     * Please note that {@link cz.msebera.android.httpclient.conn.HttpInetSocketAddress} class should
     * be used in order to pass the target remote address along with the original
     * {@link cz.msebera.android.httpclient.HttpHost} value used to resolve the address. The use of
     * {@link cz.msebera.android.httpclient.conn.HttpInetSocketAddress} can also ensure that no reverse
     * DNS lookup will be performed if the target remote address was specified
     * as an IP address.
     *
     * @param sock      the socket to connect, as obtained from
     *                  {@link #createSocket(HttpParams) createSocket}.
     *                  <code>null</code> indicates that a new socket
     *                  should be created and connected.
     * @param remoteAddress the remote address to connect to.
     * @param localAddress the local address to bind the socket to, or
     *                  <code>null</code> for any
     * @param params    additional {@link HttpParams parameters} for connecting
     *
     * @return  the connected socket. The returned object may be different
     *          from the <code>sock</code> argument if this factory supports
     *          a layered protocol.
     *
     * @throws IOException if an I/O error occurs
     * @throws UnknownHostException if the IP address of the target host
     *          can not be determined
     * @throws ConnectTimeoutException if the socket cannot be connected
     *          within the time limit defined in the <code>params</code>
     *
     * @see cz.msebera.android.httpclient.conn.HttpInetSocketAddress
     */
    Socket connectSocket(
            Socket sock,
            InetSocketAddress remoteAddress,
            InetSocketAddress localAddress,
            HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException;

    /**
     * Checks whether a socket provides a secure connection. The socket must be
     * {@link #connectSocket(Socket, InetSocketAddress, InetSocketAddress, HttpParams) connected}
     * by this factory. The factory will <i>not</i> perform I/O operations in this method.
     * <p>
     * As a rule of thumb, plain sockets are not secure and TLS/SSL sockets are secure. However,
     * there may be application specific deviations. For example, a plain socket to a host in the
     * same intranet ("trusted zone") could be considered secure. On the other hand, a TLS/SSL
     * socket could be considered insecure based on the cipher suite chosen for the connection.
     *
     * @param sock      the connected socket to check
     *
     * @return  <code>true</code> if the connection of the socket
     *          should be considered secure, or
     *          <code>false</code> if it should not
     *
     * @throws IllegalArgumentException
     *  if the argument is invalid, for example because it is
     *  not a connected socket or was created by a different
     *  socket factory.
     *  Note that socket factories are <i>not</i> required to
     *  check these conditions, they may simply return a default
     *  value when called with an invalid socket argument.
     */
    boolean isSecure(Socket sock) throws IllegalArgumentException;

}
