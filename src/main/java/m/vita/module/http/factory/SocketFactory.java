package m.vita.module.http.factory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import m.vita.module.http.exception.ConnectTimeoutException;
import m.vita.module.http.header.HttpParams;

public interface SocketFactory {

    /**
     * Creates a new, unconnected socket.
     * The socket should subsequently be passed to
     * {@link #connectSocket connectSocket}.
     *
     * @return  a new socket
     *
     * @throws IOException if an I/O error occurs while creating the socket
     */
    Socket createSocket()
            throws IOException;

    /**
     * Connects a socket to the given host.
     *
     * @param sock      the socket to connect, as obtained from
     *                  {@link #createSocket createSocket}.
     *                  <code>null</code> indicates that a new socket
     *                  should be created and connected.
     * @param host      the host to connect to
     * @param port      the port to connect to on the host
     * @param localAddress the local address to bind the socket to, or
     *                  <code>null</code> for any
     * @param localPort the port on the local machine,
     *                  0 or a negative number for any
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
     */
    Socket connectSocket(
            Socket sock,
            String host,
            int port,
            InetAddress localAddress,
            int localPort,
            HttpParams params
    ) throws IOException, UnknownHostException, ConnectTimeoutException;

    /**
     * Checks whether a socket provides a secure connection.
     * The socket must be {@link #connectSocket connected}
     * by this factory.
     * The factory will <i>not</i> perform I/O operations
     * in this method.
     * <br/>
     * As a rule of thumb, plain sockets are not secure and
     * TLS/SSL sockets are secure. However, there may be
     * application specific deviations. For example, a plain
     * socket to a host in the same intranet ("trusted zone")
     * could be considered secure. On the other hand, a
     * TLS/SSL socket could be considered insecure based on
     * the cipher suite chosen for the connection.
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
    boolean isSecure(Socket sock)
            throws IllegalArgumentException;

}
