package m.vita.module.http.header;

import java.util.concurrent.TimeUnit;

import m.vita.module.http.connect.ClientConnectionRequest;
import m.vita.module.http.factory.SchemeRegistry;


public interface ClientConnectionManager {

    /**
     * Obtains the scheme registry used by this manager.
     *
     * @return  the scheme registry, never <code>null</code>
     */
    SchemeRegistry getSchemeRegistry();

    /**
     * Returns a new {@link ClientConnectionRequest}, from which a
     * {@link ManagedClientConnection} can be obtained or the request can be
     * aborted.
     */
    ClientConnectionRequest requestConnection(HttpRoute route, Object state);

    /**
     * Releases a connection for use by others.
     * You may optionally specify how long the connection is valid
     * to be reused.  Values <= 0 are considered to be valid forever.
     * If the connection is not marked as reusable, the connection will
     * not be reused regardless of the valid duration.
     *
     * If the connection has been released before,
     * the call will be ignored.
     *
     * @param conn      the connection to release
     * @param validDuration the duration of time this connection is valid for reuse
     * @param timeUnit the unit of time validDuration is measured in
     *
     * @see #closeExpiredConnections()
     */
    void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit);

    /**
     * Closes idle connections in the pool.
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
