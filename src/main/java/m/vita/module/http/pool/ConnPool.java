package m.vita.module.http.pool;

import java.util.concurrent.Future;

import m.vita.module.http.concurrent.FutureCallback;

public interface ConnPool<T, E> {

    /**
     * Attempts to lease a connection for the given route and with the given
     * state from the pool.
     *
     * @param route route of the connection.
     * @param state arbitrary object that represents a particular state
     *  (usually a security principal or a unique token identifying
     *  the user whose credentials have been used while establishing the connection).
     *  May be <code>null</code>.
     * @param callback operation completion callback.
     *
     * @return future for a leased pool entry.
     */
    Future<E> lease(final T route, final Object state, final FutureCallback<E> callback);

    /**
     * Releases the pool entry back to the pool.
     *
     * @param entry pool entry leased from the pool
     * @param reusable flag indicating whether or not the released connection
     *   is in a consistent state and is safe for further use.
     */
    void release(E entry, boolean reusable);

}
