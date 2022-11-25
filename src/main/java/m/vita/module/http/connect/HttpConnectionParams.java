package m.vita.module.http.connect;

import m.vita.module.http.header.HttpParams;
import m.vita.module.http.util.Args;

public final class HttpConnectionParams implements CoreConnectionPNames {

    private HttpConnectionParams() {
        super();
    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#SO_TIMEOUT} parameter.
     * If not set, defaults to <code>0</code>.
     *
     * @param params HTTP parameters.
     * @return SO_TIMEOUT.
     */
    public static int getSoTimeout(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter(SO_TIMEOUT, 0);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#SO_TIMEOUT} parameter.
     *
     * @param params HTTP parameters.
     * @param timeout SO_TIMEOUT.
     */
    public static void setSoTimeout(final HttpParams params, final int timeout) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter(SO_TIMEOUT, timeout);

    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#SO_REUSEADDR} parameter.
     * If not set, defaults to <code>false</code>.
     *
     * @param params HTTP parameters.
     * @return SO_REUSEADDR.
     *
     * @since 4.1
     */
    public static boolean getSoReuseaddr(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter(SO_REUSEADDR, false);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#SO_REUSEADDR} parameter.
     *
     * @param params HTTP parameters.
     * @param reuseaddr SO_REUSEADDR.
     *
     * @since 4.1
     */
    public static void setSoReuseaddr(final HttpParams params, final boolean reuseaddr) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter(SO_REUSEADDR, reuseaddr);
    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#TCP_NODELAY} parameter.
     * If not set, defaults to <code>true</code>.
     *
     * @param params HTTP parameters.
     * @return Nagle's algorithm flag
     */
    public static boolean getTcpNoDelay(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter(TCP_NODELAY, true);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#TCP_NODELAY} parameter.
     *
     * @param params HTTP parameters.
     * @param value Nagle's algorithm flag
     */
    public static void setTcpNoDelay(final HttpParams params, final boolean value) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter(TCP_NODELAY, value);
    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#SOCKET_BUFFER_SIZE}
     * parameter. If not set, defaults to <code>-1</code>.
     *
     * @param params HTTP parameters.
     * @return socket buffer size
     */
    public static int getSocketBufferSize(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter(SOCKET_BUFFER_SIZE, -1);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#SOCKET_BUFFER_SIZE}
     * parameter.
     *
     * @param params HTTP parameters.
     * @param size socket buffer size
     */
    public static void setSocketBufferSize(final HttpParams params, final int size) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter(SOCKET_BUFFER_SIZE, size);
    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#SO_LINGER} parameter.
     * If not set, defaults to <code>-1</code>.
     *
     * @param params HTTP parameters.
     * @return SO_LINGER.
     */
    public static int getLinger(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter(SO_LINGER, -1);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#SO_LINGER} parameter.
     *
     * @param params HTTP parameters.
     * @param value SO_LINGER.
     */
    public static void setLinger(final HttpParams params, final int value) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter(SO_LINGER, value);
    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#CONNECTION_TIMEOUT}
     * parameter. If not set, defaults to <code>0</code>.
     *
     * @param params HTTP parameters.
     * @return connect timeout.
     */
    public static int getConnectionTimeout(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter(CONNECTION_TIMEOUT, 0);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#CONNECTION_TIMEOUT}
     * parameter.
     *
     * @param params HTTP parameters.
     * @param timeout connect timeout.
     */
    public static void setConnectionTimeout(final HttpParams params, final int timeout) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter(CONNECTION_TIMEOUT, timeout);
    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#STALE_CONNECTION_CHECK}
     * parameter. If not set, defaults to <code>true</code>.
     *
     * @param params HTTP parameters.
     * @return stale connection check flag.
     */
    public static boolean isStaleCheckingEnabled(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter(STALE_CONNECTION_CHECK, true);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#STALE_CONNECTION_CHECK}
     * parameter.
     *
     * @param params HTTP parameters.
     * @param value stale connection check flag.
     */
    public static void setStaleCheckingEnabled(final HttpParams params, final boolean value) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter(STALE_CONNECTION_CHECK, value);
    }

    /**
     * Obtains value of the {@link CoreConnectionPNames#SO_KEEPALIVE} parameter.
     * If not set, defaults to <code>false</code>.
     *
     * @param params HTTP parameters.
     * @return SO_KEEPALIVE.
     *
     * @since 4.2
     */
    public static boolean getSoKeepalive(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter(SO_KEEPALIVE, false);
    }

    /**
     * Sets value of the {@link CoreConnectionPNames#SO_KEEPALIVE} parameter.
     *
     * @param params HTTP parameters.
     * @param enableKeepalive SO_KEEPALIVE.
     *
     * @since 4.2
     */
    public static void setSoKeepalive(final HttpParams params, final boolean enableKeepalive) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter(SO_KEEPALIVE, enableKeepalive);
    }

}
