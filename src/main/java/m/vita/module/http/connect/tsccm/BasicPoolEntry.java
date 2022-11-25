package m.vita.module.http.connect.tsccm;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;

import m.vita.module.http.client.ClientConnectionOperator;
import m.vita.module.http.client.OperatedClientConnection;
import m.vita.module.http.connect.AbstractPoolEntry;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.util.Args;

public class BasicPoolEntry extends AbstractPoolEntry {

    private final long created;

    private long updated;
    private final long validUntil;
    private long expiry;

    public BasicPoolEntry(final ClientConnectionOperator op,
                          final HttpRoute route,
                          final ReferenceQueue<Object> queue) {
        super(op, route);
        Args.notNull(route, "HTTP route");
        this.created = System.currentTimeMillis();
        this.validUntil = Long.MAX_VALUE;
        this.expiry = this.validUntil;
    }

    /**
     * Creates a new pool entry.
     *
     * @param op      the connection operator
     * @param route   the planned route for the connection
     */
    public BasicPoolEntry(final ClientConnectionOperator op,
                          final HttpRoute route) {
        this(op, route, -1, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates a new pool entry with a specified maximum lifetime.
     *
     * @param op        the connection operator
     * @param route     the planned route for the connection
     * @param connTTL   maximum lifetime of this entry, <=0 implies "infinity"
     * @param timeunit  TimeUnit of connTTL
     *
     * @since 4.1
     */
    public BasicPoolEntry(final ClientConnectionOperator op,
                          final HttpRoute route, final long connTTL, final TimeUnit timeunit) {
        super(op, route);
        Args.notNull(route, "HTTP route");
        this.created = System.currentTimeMillis();
        if (connTTL > 0) {
            this.validUntil = this.created + timeunit.toMillis(connTTL);
        } else {
            this.validUntil = Long.MAX_VALUE;
        }
        this.expiry = this.validUntil;
    }

    protected final OperatedClientConnection getConnection() {
        return super.connection;
    }

    protected final HttpRoute getPlannedRoute() {
        return super.route;
    }

    protected final BasicPoolEntryRef getWeakRef() {
        return null;
    }

    @Override
    protected void shutdownEntry() {
        super.shutdownEntry();
    }

    /**
     * @since 4.1
     */
    public long getCreated() {
        return this.created;
    }

    /**
     * @since 4.1
     */
    public long getUpdated() {
        return this.updated;
    }

    /**
     * @since 4.1
     */
    public long getExpiry() {
        return this.expiry;
    }

    public long getValidUntil() {
        return this.validUntil;
    }

    /**
     * @since 4.1
     */
    public void updateExpiry(final long time, final TimeUnit timeunit) {
        this.updated = System.currentTimeMillis();
        final long newExpiry;
        if (time > 0) {
            newExpiry = this.updated + timeunit.toMillis(time);
        } else {
            newExpiry = Long.MAX_VALUE;
        }
        this.expiry = Math.min(validUntil, newExpiry);
    }

    /**
     * @since 4.1
     */
    public boolean isExpired(final long now) {
        return now >= this.expiry;
    }

}