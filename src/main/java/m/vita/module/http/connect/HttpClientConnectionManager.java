package m.vita.module.http.connect;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import m.vita.module.http.header.HttpClientConnection;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRoute;

public interface HttpClientConnectionManager {

    /**
     * Returns a new {@link ConnectionRequest}, from which a
     * {@link HttpClientConnection} can be obtained or the request can be
     * aborted.
     * <p/>
     * Please note that newly allocated connections can be returned
     * in the closed state. The consumer of that connection is responsible
     * for fully establishing the route the to the connection target
     * by calling {@link #connect(HttpClientConnection,
     *   HttpRoute, int,
     *   HttpContext) connect} in order to connect
     * directly to the target or to the first proxy hop, optionally calling
     * {@link #upgrade(HttpClientConnection,
     *   HttpRoute,
     *   HttpContext) upgrade} method to upgrade
     * the connection after having executed <code>CONNECT</code> method to
     * all intermediate proxy hops and and finally calling {@link #routeComplete(
     *  HttpClientConnection,
     *  HttpRoute,
     *  HttpContext) routeComplete} to mark the route
     *  as fully completed.
     *
     * @param route HTTP route of the requested connection.
     * @param state expected state of the connection or <code>null</code>
     *              if the connection is not expected to carry any state.
     */
    ConnectionRequest requestConnection(HttpRoute route, Object state);

    /**
     * Releases the connection back to the manager making it potentially
     * re-usable by other consumers. Optionally, the maximum period
     * of how long the manager should keep the connection alive can be
     * defined using <code>validDuration</code> and <code>timeUnit</code>
     * parameters.
     *
     * @param conn      the managed connection to release.
     * @param validDuration the duration of time this connection is valid for reuse.
     * @param timeUnit the time unit.
     *
     * @see #closeExpiredConnections()
     */
    void releaseConnection(
            HttpClientConnection conn, Object newState, long validDuration, TimeUnit timeUnit);

    /**
     * Connects the underlying connection socket to the connection target in case
     * of a direct route or to the first proxy hop in case of a route via a proxy
     * (or multiple proxies).
     *
     * @param conn the managed connection.
     * @param route the route of the connection.
     * @param connectTimeout connect timeout in milliseconds.
     * @param context the actual HTTP context.
     * @throws IOException
     */
    void connect(
            HttpClientConnection conn,
            HttpRoute route,
            int connectTimeout,
            HttpContext context) throws IOException;

    /**
     * Upgrades the underlying connection socket to TLS/SSL (or another layering
     * protocol) after having executed <code>CONNECT</code> method to all
     * intermediate proxy hops
     *
     * @param conn the managed connection.
     * @param route the route of the connection.
     * @param context the actual HTTP context.
     * @throws IOException
     */
    void upgrade(
            HttpClientConnection conn,
            HttpRoute route,
            HttpContext context) throws IOException;

    /**
     * Marks the connection as fully established with all its intermediate
     * hops completed.
     *
     * @param conn the managed connection.
     * @param route the route of the connection.
     * @param context the actual HTTP context.
     * @throws IOException
     */
    void routeComplete(
            HttpClientConnection conn,
            HttpRoute route,
            HttpContext context) throws IOException;

    /**
     * Closes idle connections in the pool.
     * <p/>
     * Open connections in the pool that have not been used for the
     * timespan given by the argument will be closed.
     * Currently allocated connections are not subject to this method.
     * Times will be checked with milliseconds precision
     *
     * All expired connections will also be closed.
     *
     * @param idletime  the idle time of connections to be closed
     * @param tunit     the unit for the <code>idletime</code>
     *
     * @see #closeExpiredConnections()
     */
    void closeIdleConnections(long idletime, TimeUnit tunit);

    /**
     * Closes all expired connections in the pool.
     * <p/>
     * Open connections in the pool that have not been used for
     * the timespan defined when the connection was released will be closed.
     * Currently allocated connections are not subject to this method.
     * Times will be checked with milliseconds precision.
     */
    void closeExpiredConnections();

    /**
     * Shuts down this connection manager and releases allocated resources.
     * This includes closing all connections, whether they are currently
     * used or not.
     */
    void shutdown();

}
