package m.vita.module.http.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.config.HttpEntityWrapper;
import m.vita.module.http.header.ConnectionReleaseTrigger;
import m.vita.module.http.header.ManagedClientConnection;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.EntityUtils;

@NotThreadSafe
public class BasicManagedEntity extends HttpEntityWrapper
        implements ConnectionReleaseTrigger, EofSensorWatcher {

    /** The connection to release. */
    protected ManagedClientConnection managedConn;

    /** Whether to keep the connection alive. */
    protected final boolean attemptReuse;

    /**
     * Creates a new managed entity that can release a connection.
     *
     * @param entity    the entity of which to wrap the content.
     *                  Note that the argument entity can no longer be used
     *                  afterwards, since the content will be taken by this
     *                  managed entity.
     * @param conn      the connection to release
     * @param reuse     whether the connection should be re-used
     */
    public BasicManagedEntity(final HttpEntity entity,
                              final ManagedClientConnection conn,
                              final boolean reuse) {
        super(entity);
        Args.notNull(conn, "Connection");
        this.managedConn = conn;
        this.attemptReuse = reuse;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(wrappedEntity.getContent(), this);
    }

    private void ensureConsumed() throws IOException {
        if (managedConn == null) {
            return;
        }

        try {
            if (attemptReuse) {
                // this will not trigger a callback from EofSensorInputStream
                EntityUtils.consume(wrappedEntity);
                managedConn.markReusable();
            } else {
                managedConn.unmarkReusable();
            }
        } finally {
            releaseManagedConnection();
        }
    }

    /**
     * @deprecated (4.1) Use {@link EntityUtils#consume(HttpEntity)}
     */
    @Deprecated
    @Override
    public void consumeContent() throws IOException {
        ensureConsumed();
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(outstream);
        ensureConsumed();
    }

    public void releaseConnection() throws IOException {
        ensureConsumed();
    }

    public void abortConnection() throws IOException {

        if (managedConn != null) {
            try {
                managedConn.abortConnection();
            } finally {
                managedConn = null;
            }
        }
    }

    public boolean eofDetected(final InputStream wrapped) throws IOException {
        try {
            if (managedConn != null) {
                if (attemptReuse) {
                    // there may be some cleanup required, such as
                    // reading trailers after the response body:
                    wrapped.close();
                    managedConn.markReusable();
                } else {
                    managedConn.unmarkReusable();
                }
            }
        } finally {
            releaseManagedConnection();
        }
        return false;
    }

    public boolean streamClosed(final InputStream wrapped) throws IOException {
        try {
            if (managedConn != null) {
                if (attemptReuse) {
                    final boolean valid = managedConn.isOpen();
                    // this assumes that closing the stream will
                    // consume the remainder of the response body:
                    try {
                        wrapped.close();
                        managedConn.markReusable();
                    } catch (final SocketException ex) {
                        if (valid) {
                            throw ex;
                        }
                    }
                } else {
                    managedConn.unmarkReusable();
                }
            }
        } finally {
            releaseManagedConnection();
        }
        return false;
    }

    public boolean streamAbort(final InputStream wrapped) throws IOException {
        if (managedConn != null) {
            managedConn.abortConnection();
        }
        return false;
    }

    /**
     * Releases the connection gracefully.
     * The connection attribute will be nullified.
     * Subsequent invocations are no-ops.
     *
     * @throws IOException      in case of an IO problem.
     *         The connection attribute will be nullified anyway.
     */
    protected void releaseManagedConnection()
            throws IOException {

        if (managedConn != null) {
            try {
                managedConn.releaseConnection();
            } finally {
                managedConn = null;
            }
        }
    }

}
