package m.vita.module.http.connect;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import m.vita.module.http.client.OperatedClientConnection;
import m.vita.module.http.connect.route.RouteTracker;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.util.HttpClientAndroidLog;

class HttpPoolEntry extends PoolEntry<HttpRoute, OperatedClientConnection> {

    public HttpClientAndroidLog log;
    private final RouteTracker tracker;

    public HttpPoolEntry(
            final HttpClientAndroidLog log,
            final String id,
            final HttpRoute route,
            final OperatedClientConnection conn,
            final long timeToLive, final TimeUnit tunit) {
        super(id, route, conn, timeToLive, tunit);
        this.log = log;
        this.tracker = new RouteTracker(route);
    }

    @Override
    public boolean isExpired(final long now) {
        final boolean expired = super.isExpired(now);
        if (expired && this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
        }
        return expired;
    }

    RouteTracker getTracker() {
        return this.tracker;
    }

    HttpRoute getPlannedRoute() {
        return getRoute();
    }

    HttpRoute getEffectiveRoute() {
        return this.tracker.toRoute();
    }

    @Override
    public boolean isClosed() {
        final OperatedClientConnection conn = getConnection();
        return !conn.isOpen();
    }

    @Override
    public void close() {
        final OperatedClientConnection conn = getConnection();
        try {
            conn.close();
        } catch (final IOException ex) {
            this.log.debug("I/O error closing connection", ex);
        }
    }

}
