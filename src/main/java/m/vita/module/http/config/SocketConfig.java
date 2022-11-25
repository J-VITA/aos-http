package m.vita.module.http.config;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.util.Args;

@Immutable
public class SocketConfig implements Cloneable {

    public static final SocketConfig DEFAULT = new Builder().build();

    private final int soTimeout;
    private final boolean soReuseAddress;
    private final int soLinger;
    private final boolean soKeepAlive;
    private final boolean tcpNoDelay;

    SocketConfig(
            final int soTimeout,
            final boolean soReuseAddress,
            final int soLinger,
            final boolean soKeepAlive,
            final boolean tcpNoDelay) {
        super();
        this.soTimeout = soTimeout;
        this.soReuseAddress = soReuseAddress;
        this.soLinger = soLinger;
        this.soKeepAlive = soKeepAlive;
        this.tcpNoDelay = tcpNoDelay;
    }

    /**
     * Determines the default socket timeout value for non-blocking I/O operations.
     * <p/>
     * Default: <code>0</code> (no timeout)
     *
     * @see java.net.SocketOptions#SO_TIMEOUT
     */
    public int getSoTimeout() {
        return soTimeout;
    }

    /**
     * Determines the default value of the {@link java.net.SocketOptions#SO_REUSEADDR} parameter
     * for newly created sockets.
     * <p/>
     * Default: <code>false</code>
     *
     * @see java.net.SocketOptions#SO_REUSEADDR
     */
    public boolean isSoReuseAddress() {
        return soReuseAddress;
    }

    /**
     * Determines the default value of the {@link java.net.SocketOptions#SO_LINGER} parameter
     * for newly created sockets.
     * <p/>
     * Default: <code>-1</code>
     *
     * @see java.net.SocketOptions#SO_LINGER
     */
    public int getSoLinger() {
        return soLinger;
    }

    /**
     * Determines the default value of the {@link java.net.SocketOptions#SO_KEEPALIVE} parameter
     * for newly created sockets.
     * <p/>
     * Default: <code>-1</code>
     *
     * @see java.net.SocketOptions#SO_KEEPALIVE
     */
    public boolean isSoKeepAlive() {
        return this.soKeepAlive;
    }

    /**
     * Determines the default value of the {@link java.net.SocketOptions#TCP_NODELAY} parameter
     * for newly created sockets.
     * <p/>
     * Default: <code>false</code>
     *
     * @see java.net.SocketOptions#TCP_NODELAY
     */
    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    @Override
    protected SocketConfig clone() throws CloneNotSupportedException {
        return (SocketConfig) super.clone();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[soTimeout=").append(this.soTimeout)
                .append(", soReuseAddress=").append(this.soReuseAddress)
                .append(", soLinger=").append(this.soLinger)
                .append(", soKeepAlive=").append(this.soKeepAlive)
                .append(", tcpNoDelay=").append(this.tcpNoDelay)
                .append("]");
        return builder.toString();
    }

    public static Builder custom() {
        return new Builder();
    }

    public static Builder copy(final SocketConfig config) {
        Args.notNull(config, "Socket config");
        return new Builder()
                .setSoTimeout(config.getSoTimeout())
                .setSoReuseAddress(config.isSoReuseAddress())
                .setSoLinger(config.getSoLinger())
                .setSoKeepAlive(config.isSoKeepAlive())
                .setTcpNoDelay(config.isTcpNoDelay());
    }

    public static class Builder {

        private int soTimeout;
        private boolean soReuseAddress;
        private int soLinger;
        private boolean soKeepAlive;
        private boolean tcpNoDelay;

        Builder() {
            this.soLinger = -1;
            this.tcpNoDelay = true;
        }

        public Builder setSoTimeout(final int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder setSoReuseAddress(final boolean soReuseAddress) {
            this.soReuseAddress = soReuseAddress;
            return this;
        }

        public Builder setSoLinger(final int soLinger) {
            this.soLinger = soLinger;
            return this;
        }

        public Builder setSoKeepAlive(final boolean soKeepAlive) {
            this.soKeepAlive = soKeepAlive;
            return this;
        }

        public Builder setTcpNoDelay(final boolean tcpNoDelay) {
            this.tcpNoDelay = tcpNoDelay;
            return this;
        }

        public SocketConfig build() {
            return new SocketConfig(soTimeout, soReuseAddress, soLinger, soKeepAlive, tcpNoDelay);
        }

    }

}
