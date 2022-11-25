package m.vita.module.http.params;

import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.ManagedClientConnection;

public interface ConnManagerPNames {

    /**
     * Defines the timeout in milliseconds used when retrieving an instance of
     * {@link ManagedClientConnection} from the
     * {@link ClientConnectionManager}.
     * <p>
     * This parameter expects a value of type {@link Long}.
     */
    public static final String TIMEOUT = "http.conn-manager.timeout";

    /**
     * Defines the maximum number of connections per route.
     * This limit is interpreted by client connection managers
     * and applies to individual manager instances.
     * <p>
     * This parameter expects a value of type {@link ConnPerRoute}.
     * <p>
     */
    public static final String MAX_CONNECTIONS_PER_ROUTE = "http.conn-manager.max-per-route";

    /**
     * Defines the maximum number of connections in total.
     * This limit is interpreted by client connection managers
     * and applies to individual manager instances.
     * <p>
     * This parameter expects a value of type {@link Integer}.
     */
    public static final String MAX_TOTAL_CONNECTIONS = "http.conn-manager.max-total";

}
