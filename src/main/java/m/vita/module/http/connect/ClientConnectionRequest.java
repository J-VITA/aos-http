package m.vita.module.http.connect;

import java.util.concurrent.TimeUnit;

import m.vita.module.http.exception.ConnectionPoolTimeoutException;
import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.ManagedClientConnection;

public interface ClientConnectionRequest {

    /**
     * Obtains a connection within a given time.
     * This method will block until a connection becomes available,
     * the timeout expires, or the connection manager is
     * {@link ClientConnectionManager#shutdown() shut down}.
     * Timeouts are handled with millisecond precision.
     *
     * If {@link #abortRequest()} is called while this is blocking or
     * before this began, an {@link InterruptedException} will
     * be thrown.
     *
     * @param timeout   the timeout, 0 or negative for no timeout
     * @param tunit     the unit for the <code>timeout</code>,
     *                  may be <code>null</code> only if there is no timeout
     *
     * @return  a connection that can be used to communicate
     *          along the given route
     *
     * @throws ConnectionPoolTimeoutException
     *         in case of a timeout
     * @throws InterruptedException
     *         if the calling thread is interrupted while waiting
     */
    ManagedClientConnection getConnection(long timeout, TimeUnit tunit)
            throws InterruptedException, ConnectionPoolTimeoutException;

    /**
     * Aborts the call to {@link #getConnection(long, TimeUnit)},
     * causing it to throw an {@link InterruptedException}.
     */
    void abortRequest();

}
