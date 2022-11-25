package m.vita.module.http.connect.tsccm;

import java.util.concurrent.TimeUnit;

import m.vita.module.http.exception.ConnectionPoolTimeoutException;

public interface PoolEntryRequest {

    /**
     * Obtains a pool entry with a connection within the given timeout.
     * If {@link #abortRequest()} is called before this completes
     * an {@link InterruptedException} is thrown.
     *
     * @param timeout   the timeout, 0 or negative for no timeout
     * @param tunit     the unit for the <code>timeout</code>,
     *                  may be <code>null</code> only if there is no timeout
     *
     * @return  pool entry holding a connection for the route
     *
     * @throws ConnectionPoolTimeoutException
     *         if the timeout expired
     * @throws InterruptedException
     *         if the calling thread was interrupted or the request was aborted
     */
    BasicPoolEntry getPoolEntry(
            long timeout,
            TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException;

    /**
     * Aborts the active or next call to
     * {@link #getPoolEntry(long, TimeUnit)}.
     */
    void abortRequest();

}
