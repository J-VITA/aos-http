package m.vita.module.http.connect;

import androidx.annotation.GuardedBy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.ClientConnectionOperator;
import m.vita.module.http.client.OperatedClientConnection;
import m.vita.module.http.factory.SchemeRegistry;
import m.vita.module.http.factory.SchemeRegistryFactory;
import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.HttpClientConnection;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.ManagedClientConnection;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.Asserts;
import m.vita.module.http.util.HttpClientAndroidLog;

@ThreadSafe
@Deprecated
public class BasicClientConnectionManager implements ClientConnectionManager {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private static final AtomicLong COUNTER = new AtomicLong();

    /** The message to be logged on multiple allocation. */
    public final static String MISUSE_MESSAGE =
            "Invalid use of BasicClientConnManager: connection still allocated.\n" +
                    "Make sure to release the connection before allocating another one.";

    /** The schemes supported by this connection manager. */
    private final SchemeRegistry schemeRegistry;

    /** The operator for opening and updating connections. */
    private final ClientConnectionOperator connOperator;

    /** The one and only entry in this pool. */
    @GuardedBy("this")
    private HttpPoolEntry poolEntry;

    /** The currently issued managed connection, if any. */
    @GuardedBy("this")
    private ManagedClientConnectionImpl conn;

    /** Indicates whether this connection manager is shut down. */
    @GuardedBy("this")
    private volatile boolean shutdown;

    /**
     * Creates a new simple connection manager.
     *
     * @param schreg    the scheme registry
     */
    public BasicClientConnectionManager(final SchemeRegistry schreg) {
        Args.notNull(schreg, "Scheme registry");
        this.schemeRegistry = schreg;
        this.connOperator = createConnectionOperator(schreg);
    }

    public BasicClientConnectionManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally { // Make sure we call overridden method even if shutdown barfs
            super.finalize();
        }
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    protected ClientConnectionOperator createConnectionOperator(final SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }

    public final ClientConnectionRequest requestConnection(
            final HttpRoute route,
            final Object state) {

        return new ClientConnectionRequest() {

            public void abortRequest() {
                // Nothing to abort, since requests are immediate.
            }

            public ManagedClientConnection getConnection(
                    final long timeout, final TimeUnit tunit) {
                return BasicClientConnectionManager.this.getConnection(
                        route, state);
            }

        };
    }

    private void assertNotShutdown() {
        Asserts.check(!this.shutdown, "Connection manager has been shut down");
    }

    ManagedClientConnection getConnection(final HttpRoute route, final Object state) {
        Args.notNull(route, "Route");
        synchronized (this) {
            assertNotShutdown();
            if (this.log.isDebugEnabled()) {
                this.log.debug("Get connection for route " + route);
            }
            Asserts.check(this.conn == null, MISUSE_MESSAGE);
            if (this.poolEntry != null && !this.poolEntry.getPlannedRoute().equals(route)) {
                this.poolEntry.close();
                this.poolEntry = null;
            }
            if (this.poolEntry == null) {
                final String id = Long.toString(COUNTER.getAndIncrement());
                final OperatedClientConnection conn = this.connOperator.createConnection();
                this.poolEntry = new HttpPoolEntry(this.log, id, route, conn, 0, TimeUnit.MILLISECONDS);
            }
            final long now = System.currentTimeMillis();
            if (this.poolEntry.isExpired(now)) {
                this.poolEntry.close();
                this.poolEntry.getTracker().reset();
            }
            this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
            return this.conn;
        }
    }

    private void shutdownConnection(final HttpClientConnection conn) {
        try {
            conn.shutdown();
        } catch (final IOException iox) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("I/O exception shutting down connection", iox);
            }
        }
    }

    public void releaseConnection(final ManagedClientConnection conn, final long keepalive, final TimeUnit tunit) {
        Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, " +
                "connection not obtained from this manager");
        final ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl) conn;
        synchronized (managedConn) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Releasing connection " + conn);
            }
            if (managedConn.getPoolEntry() == null) {
                return; // already released
            }
            final ClientConnectionManager manager = managedConn.getManager();
            Asserts.check(manager == this, "Connection not obtained from this manager");
            synchronized (this) {
                if (this.shutdown) {
                    shutdownConnection(managedConn);
                    return;
                }
                try {
                    if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
                        shutdownConnection(managedConn);
                    }
                    if (managedConn.isMarkedReusable()) {
                        this.poolEntry.updateExpiry(keepalive, tunit != null ? tunit : TimeUnit.MILLISECONDS);
                        if (this.log.isDebugEnabled()) {
                            final String s;
                            if (keepalive > 0) {
                                s = "for " + keepalive + " " + tunit;
                            } else {
                                s = "indefinitely";
                            }
                            this.log.debug("Connection can be kept alive " + s);
                        }
                    }
                } finally {
                    managedConn.detach();
                    this.conn = null;
                    if (this.poolEntry.isClosed()) {
                        this.poolEntry = null;
                    }
                }
            }
        }
    }

    public void closeExpiredConnections() {
        synchronized (this) {
            assertNotShutdown();
            final long now = System.currentTimeMillis();
            if (this.poolEntry != null && this.poolEntry.isExpired(now)) {
                this.poolEntry.close();
                this.poolEntry.getTracker().reset();
            }
        }
    }

    public void closeIdleConnections(final long idletime, final TimeUnit tunit) {
        Args.notNull(tunit, "Time unit");
        synchronized (this) {
            assertNotShutdown();
            long time = tunit.toMillis(idletime);
            if (time < 0) {
                time = 0;
            }
            final long deadline = System.currentTimeMillis() - time;
            if (this.poolEntry != null && this.poolEntry.getUpdated() <= deadline) {
                this.poolEntry.close();
                this.poolEntry.getTracker().reset();
            }
        }
    }

    public void shutdown() {
        synchronized (this) {
            this.shutdown = true;
            try {
                if (this.poolEntry != null) {
                    this.poolEntry.close();
                }
            } finally {
                this.poolEntry = null;
                this.conn = null;
            }
        }
    }

}
