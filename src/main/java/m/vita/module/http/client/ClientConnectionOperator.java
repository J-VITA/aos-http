package m.vita.module.http.client;

import java.io.IOException;
import java.net.InetAddress;

import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;

public interface ClientConnectionOperator {

    /**
     * Creates a new connection that can be operated.
     *
     * @return  a new, unopened connection for use with this operator
     */
    OperatedClientConnection createConnection();

    /**
     * Opens a connection to the given target host.
     *
     * @param conn      the connection to open
     * @param target    the target host to connect to
     * @param local     the local address to route from, or
     *                  <code>null</code> for the default
     * @param context   the context for the connection
     * @param params    the parameters for the connection
     *
     * @throws IOException      in case of a problem
     */
    void openConnection(OperatedClientConnection conn,
                        HttpHost target,
                        InetAddress local,
                        HttpContext context,
                        HttpParams params)
            throws IOException;

    /**
     * Updates a connection with a layered secure connection.
     * The typical use of this method is to update a tunnelled plain
     * connection (HTTP) to a secure TLS/SSL connection (HTTPS).
     *
     * @param conn      the open connection to update
     * @param target    the target host for the updated connection.
     *                  The connection must already be open or tunnelled
     *                  to the host and port, but the scheme of the target
     *                  will be used to create a layered connection.
     * @param context   the context for the connection
     * @param params    the parameters for the updated connection
     *
     * @throws IOException      in case of a problem
     */
    void updateSecureConnection(OperatedClientConnection conn,
                                HttpHost target,
                                HttpContext context,
                                HttpParams params)
            throws IOException;

}

