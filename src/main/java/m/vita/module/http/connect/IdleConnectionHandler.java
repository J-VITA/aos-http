package m.vita.module.http.connect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import m.vita.module.http.util.HttpClientAndroidLog;

public class IdleConnectionHandler {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    /** Holds connections and the time they were added. */
    private final Map<HttpConnection,TimeValues> connectionToTimes;


    public IdleConnectionHandler() {
        super();
        connectionToTimes = new HashMap<HttpConnection,TimeValues>();
    }

    /**
     * Registers the given connection with this handler.  The connection will be held until
     * {@link #remove} or {@link #closeIdleConnections} is called.
     *
     * @param connection the connection to add
     *
     * @see #remove
     */
    public void add(final HttpConnection connection, final long validDuration, final TimeUnit unit) {

        final long timeAdded = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug("Adding connection at: " + timeAdded);
        }

        connectionToTimes.put(connection, new TimeValues(timeAdded, validDuration, unit));
    }

    /**
     * Removes the given connection from the list of connections to be closed when idle.
     * This will return true if the connection is still valid, and false
     * if the connection should be considered expired and not used.
     *
     * @param connection
     * @return True if the connection is still valid.
     */
    public boolean remove(final HttpConnection connection) {
        final TimeValues times = connectionToTimes.remove(connection);
        if(times == null) {
            log.warn("Removing a connection that never existed!");
            return true;
        } else {
            return System.currentTimeMillis() <= times.timeExpires;
        }
    }

    /**
     * Removes all connections referenced by this handler.
     */
    public void removeAll() {
        this.connectionToTimes.clear();
    }

    /**
     * Closes connections that have been idle for at least the given amount of time.
     *
     * @param idleTime the minimum idle time, in milliseconds, for connections to be closed
     */
    public void closeIdleConnections(final long idleTime) {

        // the latest time for which connections will be closed
        final long idleTimeout = System.currentTimeMillis() - idleTime;

        if (log.isDebugEnabled()) {
            log.debug("Checking for connections, idle timeout: "  + idleTimeout);
        }

        for (final Map.Entry<HttpConnection, TimeValues> entry : connectionToTimes.entrySet()) {
            final HttpConnection conn = entry.getKey();
            final TimeValues times = entry.getValue();
            final long connectionTime = times.timeAdded;
            if (connectionTime <= idleTimeout) {
                if (log.isDebugEnabled()) {
                    log.debug("Closing idle connection, connection time: "  + connectionTime);
                }
                try {
                    conn.close();
                } catch (final IOException ex) {
                    log.debug("I/O error closing connection", ex);
                }
            }
        }
    }


    public void closeExpiredConnections() {
        final long now = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("Checking for expired connections, now: "  + now);
        }

        for (final Map.Entry<HttpConnection, TimeValues> entry : connectionToTimes.entrySet()) {
            final HttpConnection conn = entry.getKey();
            final TimeValues times = entry.getValue();
            if(times.timeExpires <= now) {
                if (log.isDebugEnabled()) {
                    log.debug("Closing connection, expired @: "  + times.timeExpires);
                }
                try {
                    conn.close();
                } catch (final IOException ex) {
                    log.debug("I/O error closing connection", ex);
                }
            }
        }
    }

    private static class TimeValues {
        private final long timeAdded;
        private final long timeExpires;

        /**
         * @param now The current time in milliseconds
         * @param validDuration The duration this connection is valid for
         * @param validUnit The unit of time the duration is specified in.
         */
        TimeValues(final long now, final long validDuration, final TimeUnit validUnit) {
            this.timeAdded = now;
            if(validDuration > 0) {
                this.timeExpires = now + validUnit.toMillis(validDuration);
            } else {
                this.timeExpires = Long.MAX_VALUE;
            }
        }
    }
}
