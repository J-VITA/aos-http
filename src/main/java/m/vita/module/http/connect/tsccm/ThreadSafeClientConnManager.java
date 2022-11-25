package m.vita.module.http.connect.tsccm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.ClientConnectionOperator;
import m.vita.module.http.connect.ClientConnectionRequest;
import m.vita.module.http.connect.DefaultClientConnectionOperator;
import m.vita.module.http.exception.ConnectionPoolTimeoutException;
import m.vita.module.http.factory.SchemeRegistry;
import m.vita.module.http.factory.SchemeRegistryFactory;
import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.ManagedClientConnection;
import m.vita.module.http.params.ConnPerRouteBean;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.Asserts;
import m.vita.module.http.util.HttpClientAndroidLog;

@ThreadSafe
public class ThreadSafeClientConnManager implements ClientConnectionManager {

    public HttpClientAndroidLog log;

    /** The schemes supported by this connection manager. */
    protected final SchemeRegistry schemeRegistry; // @ThreadSafe

    protected final AbstractConnPool connectionPool;

    /** The pool of connections being managed. */
    protected final ConnPoolByRoute pool;

    /** The operator for opening and updating connections. */
    protected final ClientConnectionOperator connOperator; // DefaultClientConnectionOperator is @ThreadSafe

    protected final ConnPerRouteBean connPerRoute;

    /**
     * Creates a new thread safe connection manager.
     *
     * @param schreg    the scheme registry.
     */
    public ThreadSafeClientConnManager(final SchemeRegistry schreg) {
        this(schreg, -1, TimeUnit.MILLISECONDS);
    }

    /**
     * @since 4.1
     */
    public ThreadSafeClientConnManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    /**
     * Creates a new thread safe connection manager.
     *
     * @param schreg    the scheme registry.
     * @param connTTL   max connection lifetime, <=0 implies "infinity"
     * @param connTTLTimeUnit   TimeUnit of connTTL
     *
     * @since 4.1
     */
    public ThreadSafeClientConnManager(final SchemeRegistry schreg,
                                       final long connTTL, final TimeUnit connTTLTimeUnit) {
        this(schreg, connTTL, connTTLTimeUnit, new ConnPerRouteBean());
    }

    /**
     * Creates a new thread safe connection manager.
     *
     * @param schreg    the scheme registry.
     * @param connTTL   max connection lifetime, <=0 implies "infinity"
     * @param connTTLTimeUnit   TimeUnit of connTTL
     * @param connPerRoute    mapping of maximum connections per route,
     *   provided as a dependency so it can be managed externally, e.g.
     *   for dynamic connection pool size management.
     *
     * @since 4.2
     */
    public ThreadSafeClientConnManager(final SchemeRegistry schreg,
                                       final long connTTL, final TimeUnit connTTLTimeUnit, final ConnPerRouteBean connPerRoute) {
        super();
        Args.notNull(schreg, "Scheme registry");
        this.log = new HttpClientAndroidLog(getClass());
        this.schemeRegistry = schreg;
        this.connPerRoute = connPerRoute;
        this.connOperator = createConnectionOperator(schreg);
        this.pool = createConnectionPool(connTTL, connTTLTimeUnit) ;
        this.connectionPool = this.pool;
    }

    /**
     * Creates a new thread safe connection manager.
     *
     * @param params    the parameters for this manager.
     * @param schreg    the scheme registry.
     *
     */
    public ThreadSafeClientConnManager(final HttpParams params,
                                       final SchemeRegistry schreg) {
        Args.notNull(schreg, "Scheme registry");
        this.log = new HttpClientAndroidLog(getClass());
        this.schemeRegistry = schreg;
        this.connPerRoute = new ConnPerRouteBean();
        this.connOperator = createConnectionOperator(schreg);
        this.pool = (ConnPoolByRoute) createConnectionPool(params) ;
        this.connectionPool = this.pool;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    /**
     * Hook for creating the connection pool.
     *
     * @return  the connection pool to use
     *
     * @deprecated (4.1)  use #createConnectionPool(long, TimeUnit))
     */
    @Deprecated
    protected AbstractConnPool createConnectionPool(final HttpParams params) {
        return new ConnPoolByRoute(connOperator, params);
    }

    /**
     * Hook for creating the connection pool.
     *
     * @return  the connection pool to use
     *
     * @since 4.1
     */
    protected ConnPoolByRoute createConnectionPool(final long connTTL, final TimeUnit connTTLTimeUnit) {
        return new ConnPoolByRoute(connOperator, connPerRoute, 20, connTTL, connTTLTimeUnit);
    }

    /**
     * Hook for creating the connection operator.
     * It is called by the constructor.
     * Derived classes can override this method to change the
     * instantiation of the operator.
     * The default implementation here instantiates
     * {@link DefaultClientConnectionOperator DefaultClientConnectionOperator}.
     *
     * @param schreg    the scheme registry.
     *
     * @return  the connection operator to use
     */
    protected ClientConnectionOperator
    createConnectionOperator(final SchemeRegistry schreg) {

        return new DefaultClientConnectionOperator(schreg);// @ThreadSafe
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    public ClientConnectionRequest requestConnection(
            final HttpRoute route,
            final Object state) {

        final PoolEntryRequest poolRequest = pool.requestPoolEntry(
                route, state);

        return new ClientConnectionRequest() {

            public void abortRequest() {
                poolRequest.abortRequest();
            }

            public ManagedClientConnection getConnection(
                    final long timeout, final TimeUnit tunit) throws InterruptedException,
                    ConnectionPoolTimeoutException {
                Args.notNull(route, "Route");

                if (log.isDebugEnabled()) {
                    log.debug("Get connection: " + route + ", timeout = " + timeout);
                }

                final BasicPoolEntry entry = poolRequest.getPoolEntry(timeout, tunit);
                return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, entry);
            }

        };

    }

    public void releaseConnection(final ManagedClientConnection conn, final long validDuration, final TimeUnit timeUnit) {
        Args.check(conn instanceof BasicPooledConnAdapter, "Connection class mismatch, " +
                "connection not obtained from this manager");
        final BasicPooledConnAdapter hca = (BasicPooledConnAdapter) conn;
        if (hca.getPoolEntry() != null) {
            Asserts.check(hca.getManager() == this, "Connection not obtained from this manager");
        }
        synchronized (hca) {
            final BasicPoolEntry entry = (BasicPoolEntry) hca.getPoolEntry();
            if (entry == null) {
                return;
            }
            try {
                // make sure that the response has been read completely
                if (hca.isOpen() && !hca.isMarkedReusable()) {
                    // In MTHCM, there would be a call to
                    // SimpleHttpConnectionManager.finishLastResponse(conn);
                    // Consuming the response is handled outside in 4.0.

                    // make sure this connection will not be re-used
                    // Shut down rather than close, we might have gotten here
                    // because of a shutdown trigger.
                    // Shutdown of the adapter also clears the tracked route.
                    hca.shutdown();
                }
            } catch (final IOException iox) {
                if (log.isDebugEnabled()) {
                    log.debug("Exception shutting down released connection.",
                            iox);
                }
            } finally {
                final boolean reusable = hca.isMarkedReusable();
                if (log.isDebugEnabled()) {
                    if (reusable) {
                        log.debug("Released connection is reusable.");
                    } else {
                        log.debug("Released connection is not reusable.");
                    }
                }
                hca.detach();
                pool.freeEntry(entry, reusable, validDuration, timeUnit);
            }
        }
    }

    public void shutdown() {
        log.debug("Shutting down");
        pool.shutdown();
    }

    /**
     * Gets the total number of pooled connections for the given route.
     * This is the total number of connections that have been created and
     * are still in use by this connection manager for the route.
     * This value will not exceed the maximum number of connections per host.
     *
     * @param route     the route in question
     *
     * @return  the total number of pooled connections for that route
     */
    public int getConnectionsInPool(final HttpRoute route) {
        return pool.getConnectionsInPool(route);
    }

    /**
     * Gets the total number of pooled connections.  This is the total number of
     * connections that have been created and are still in use by this connection
     * manager.  This value will not exceed the maximum number of connections
     * in total.
     *
     * @return the total number of pooled connections
     */
    public int getConnectionsInPool() {
        return pool.getConnectionsInPool();
    }

    public void closeIdleConnections(final long idleTimeout, final TimeUnit tunit) {
        if (log.isDebugEnabled()) {
            log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
        }
        pool.closeIdleConnections(idleTimeout, tunit);
    }

    public void closeExpiredConnections() {
        log.debug("Closing expired connections");
        pool.closeExpiredConnections();
    }

    /**
     * since 4.1
     */
    public int getMaxTotal() {
        return pool.getMaxTotalConnections();
    }

    /**
     * since 4.1
     */
    public void setMaxTotal(final int max) {
        pool.setMaxTotalConnections(max);
    }

    /**
     * @since 4.1
     */
    public int getDefaultMaxPerRoute() {
        return connPerRoute.getDefaultMaxPerRoute();
    }

    /**
     * @since 4.1
     */
    public void setDefaultMaxPerRoute(final int max) {
        connPerRoute.setDefaultMaxPerRoute(max);
    }

    /**
     * @since 4.1
     */
    public int getMaxForRoute(final HttpRoute route) {
        return connPerRoute.getMaxForRoute(route);
    }

    /**
     * @since 4.1
     */
    public void setMaxForRoute(final HttpRoute route, final int max) {
        connPerRoute.setMaxForRoute(route, max);
    }

}

