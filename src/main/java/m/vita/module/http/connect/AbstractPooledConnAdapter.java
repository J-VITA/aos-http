package m.vita.module.http.connect;

import java.io.IOException;

import m.vita.module.http.client.OperatedClientConnection;
import m.vita.module.http.exception.ConnectionShutdownException;
import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRoute;

public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter {

    /** The wrapped pool entry. */
    protected volatile AbstractPoolEntry poolEntry;

    /**
     * Creates a new connection adapter.
     *
     * @param manager   the connection manager
     * @param entry     the pool entry for the connection being wrapped
     */
    protected AbstractPooledConnAdapter(final ClientConnectionManager manager,
                                        final AbstractPoolEntry entry) {
        super(manager, entry.connection);
        this.poolEntry = entry;
    }

    public String getId() {
        return null;
    }

    /**
     * Obtains the pool entry.
     *
     * @return  the pool entry, or <code>null</code> if detached
     *
     * @deprecated (4.0.1)
     */
    @Deprecated
    protected AbstractPoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    /**
     * Asserts that there is a valid pool entry.
     *
     * @throws ConnectionShutdownException if there is no pool entry
     *                                  or connection has been aborted
     *
     * @see #assertValid(OperatedClientConnection)
     */
    protected void assertValid(final AbstractPoolEntry entry) {
        if (isReleased() || entry == null) {
            throw new ConnectionShutdownException();
        }
    }

    /**
     * @deprecated (4.1)  use {@link #assertValid(AbstractPoolEntry)}
     */
    @Deprecated
    protected final void assertAttached() {
        if (poolEntry == null) {
            throw new ConnectionShutdownException();
        }
    }

    /**
     * Detaches this adapter from the wrapped connection.
     * This adapter becomes useless.
     */
    @Override
    protected synchronized void detach() {
        poolEntry = null;
        super.detach();
    }

    public HttpRoute getRoute() {
        final AbstractPoolEntry entry = getPoolEntry();
        assertValid(entry);
        return (entry.tracker == null) ? null : entry.tracker.toRoute();
    }

    public void open(final HttpRoute route,
                     final HttpContext context, final HttpParams params)
            throws IOException {
        final AbstractPoolEntry entry = getPoolEntry();
        assertValid(entry);
        entry.open(route, context, params);
    }

    public void tunnelTarget(final boolean secure, final HttpParams params)
            throws IOException {
        final AbstractPoolEntry entry = getPoolEntry();
        assertValid(entry);
        entry.tunnelTarget(secure, params);
    }

    public void tunnelProxy(final HttpHost next, final boolean secure, final HttpParams params)
            throws IOException {
        final AbstractPoolEntry entry = getPoolEntry();
        assertValid(entry);
        entry.tunnelProxy(next, secure, params);
    }

    public void layerProtocol(final HttpContext context, final HttpParams params)
            throws IOException {
        final AbstractPoolEntry entry = getPoolEntry();
        assertValid(entry);
        entry.layerProtocol(context, params);
    }

    public void close() throws IOException {
        final AbstractPoolEntry entry = getPoolEntry();
        if (entry != null) {
            entry.shutdownEntry();
        }

        final OperatedClientConnection conn = getWrappedConnection();
        if (conn != null) {
            conn.close();
        }
    }

    public void shutdown() throws IOException {
        final AbstractPoolEntry entry = getPoolEntry();
        if (entry != null) {
            entry.shutdownEntry();
        }

        final OperatedClientConnection conn = getWrappedConnection();
        if (conn != null) {
            conn.shutdown();
        }
    }

    public Object getState() {
        final AbstractPoolEntry entry = getPoolEntry();
        assertValid(entry);
        return entry.getState();
    }

    public void setState(final Object state) {
        final AbstractPoolEntry entry = getPoolEntry();
        assertValid(entry);
        entry.setState(state);
    }

}
