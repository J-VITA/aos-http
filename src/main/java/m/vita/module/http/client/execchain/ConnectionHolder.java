package m.vita.module.http.client.execchain;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.concurrent.Cancellable;
import m.vita.module.http.connect.HttpClientConnectionManager;
import m.vita.module.http.header.ConnectionReleaseTrigger;
import m.vita.module.http.header.HttpClientConnection;
import m.vita.module.http.util.HttpClientAndroidLog;

@ThreadSafe
class ConnectionHolder implements ConnectionReleaseTrigger, Cancellable, Closeable {

    public HttpClientAndroidLog log;

    private final HttpClientConnectionManager manager;
    private final HttpClientConnection managedConn;
    private volatile boolean reusable;
    private volatile Object state;
    private volatile long validDuration;
    private volatile TimeUnit tunit;

    private volatile boolean released;

    public ConnectionHolder(
            final HttpClientAndroidLog log,
            final HttpClientConnectionManager manager,
            final HttpClientConnection managedConn) {
        super();
        this.log = log;
        this.manager = manager;
        this.managedConn = managedConn;
    }

    public boolean isReusable() {
        return this.reusable;
    }

    public void markReusable() {
        this.reusable = true;
    }

    public void markNonReusable() {
        this.reusable = false;
    }

    public void setState(final Object state) {
        this.state = state;
    }

    public void setValidFor(final long duration, final TimeUnit tunit) {
        synchronized (this.managedConn) {
            this.validDuration = duration;
            this.tunit = tunit;
        }
    }

    public void releaseConnection() {
        synchronized (this.managedConn) {
            if (this.released) {
                return;
            }
            this.released = true;
            if (this.reusable) {
                this.manager.releaseConnection(this.managedConn,
                        this.state, this.validDuration, this.tunit);
            } else {
                try {
                    this.managedConn.close();
                    log.debug("Connection discarded");
                } catch (final IOException ex) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(ex.getMessage(), ex);
                    }
                } finally {
                    this.manager.releaseConnection(
                            this.managedConn, null, 0, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    public void abortConnection() {
        synchronized (this.managedConn) {
            if (this.released) {
                return;
            }
            this.released = true;
            try {
                this.managedConn.shutdown();
                log.debug("Connection discarded");
            } catch (final IOException ex) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(ex.getMessage(), ex);
                }
            } finally {
                this.manager.releaseConnection(
                        this.managedConn, null, 0, TimeUnit.MILLISECONDS);
            }
        }
    }

    public boolean cancel() {
        final boolean alreadyReleased = this.released;
        log.debug("Cancelling request execution");
        abortConnection();
        return !alreadyReleased;
    }

    public boolean isReleased() {
        return this.released;
    }

    public void close() throws IOException {
        abortConnection();
    }

}
