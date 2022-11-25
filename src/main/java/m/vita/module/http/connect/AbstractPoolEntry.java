package m.vita.module.http.connect;

import java.io.IOException;
import java.io.InterruptedIOException;

import m.vita.module.http.client.ClientConnectionOperator;
import m.vita.module.http.client.OperatedClientConnection;
import m.vita.module.http.connect.route.RouteTracker;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.Asserts;
import m.vita.module.http.header.ManagedClientConnection;

public abstract class AbstractPoolEntry {

    /** The connection operator. */
    protected final ClientConnectionOperator connOperator;

    /** The underlying connection being pooled or used. */
    protected final OperatedClientConnection connection;

    /** The route for which this entry gets allocated. */
    //@@@ currently accessed from connection manager(s) as attribute
    //@@@ avoid that, derived classes should decide whether update is allowed
    //@@@ SCCM: yes, TSCCM: no
    protected volatile HttpRoute route;

    /** Connection state object */
    protected volatile Object state;

    /** The tracked route, or <code>null</code> before tracking starts. */
    protected volatile RouteTracker tracker;


    /**
     * Creates a new pool entry.
     *
     * @param connOperator     the Connection Operator for this entry
     * @param route   the planned route for the connection,
     *                or <code>null</code>
     */
    protected AbstractPoolEntry(final ClientConnectionOperator connOperator,
                                final HttpRoute route) {
        super();
        Args.notNull(connOperator, "Connection operator");
        this.connOperator = connOperator;
        this.connection = connOperator.createConnection();
        this.route = route;
        this.tracker = null;
    }

    /**
     * Returns the state object associated with this pool entry.
     *
     * @return The state object
     */
    public Object getState() {
        return state;
    }

    /**
     * Assigns a state object to this pool entry.
     *
     * @param state The state object
     */
    public void setState(final Object state) {
        this.state = state;
    }

    /**
     * Opens the underlying connection.
     *
     * @param route         the route along which to open the connection
     * @param context       the context for opening the connection
     * @param params        the parameters for opening the connection
     *
     * @throws IOException  in case of a problem
     */
    public void open(final HttpRoute route,
                     final HttpContext context, final HttpParams params)
            throws IOException {

        Args.notNull(route, "Route");
        Args.notNull(params, "HTTP parameters");
        if (this.tracker != null) {
            Asserts.check(!this.tracker.isConnected(), "Connection already open");
        }
        // - collect the arguments
        // - call the operator
        // - update the tracking data
        // In this order, we can be sure that only a successful
        // opening of the connection will be tracked.

        this.tracker = new RouteTracker(route);
        final HttpHost proxy  = route.getProxyHost();

        connOperator.openConnection
                (this.connection,
                        (proxy != null) ? proxy : route.getTargetHost(),
                        route.getLocalAddress(),
                        context, params);

        final RouteTracker localTracker = tracker; // capture volatile

        // If this tracker was reset while connecting,
        // fail early.
        if (localTracker == null) {
            throw new InterruptedIOException("Request aborted");
        }

        if (proxy == null) {
            localTracker.connectTarget(this.connection.isSecure());
        } else {
            localTracker.connectProxy(proxy, this.connection.isSecure());
        }

    }

    /**
     * Tracks tunnelling of the connection to the target.
     * The tunnel has to be established outside by sending a CONNECT
     * request to the (last) proxy.
     *
     * @param secure    <code>true</code> if the tunnel should be
     *                  considered secure, <code>false</code> otherwise
     * @param params    the parameters for tunnelling the connection
     *
     * @throws IOException  in case of a problem
     */
    public void tunnelTarget(final boolean secure, final HttpParams params)
            throws IOException {

        Args.notNull(params, "HTTP parameters");
        Asserts.notNull(this.tracker, "Route tracker");
        Asserts.check(this.tracker.isConnected(), "Connection not open");
        Asserts.check(!this.tracker.isTunnelled(), "Connection is already tunnelled");

        this.connection.update(null, tracker.getTargetHost(),
                secure, params);
        this.tracker.tunnelTarget(secure);
    }

    /**
     * Tracks tunnelling of the connection to a chained proxy.
     * The tunnel has to be established outside by sending a CONNECT
     * request to the previous proxy.
     *
     * @param next      the proxy to which the tunnel was established.
     *  See {@link ManagedClientConnection#tunnelProxy
     *                                  ManagedClientConnection.tunnelProxy}
     *                  for details.
     * @param secure    <code>true</code> if the tunnel should be
     *                  considered secure, <code>false</code> otherwise
     * @param params    the parameters for tunnelling the connection
     *
     * @throws IOException  in case of a problem
     */
    public void tunnelProxy(final HttpHost next, final boolean secure, final HttpParams params)
            throws IOException {

        Args.notNull(next, "Next proxy");
        Args.notNull(params, "Parameters");

        Asserts.notNull(this.tracker, "Route tracker");
        Asserts.check(this.tracker.isConnected(), "Connection not open");

        this.connection.update(null, next, secure, params);
        this.tracker.tunnelProxy(next, secure);
    }

    /**
     * Layers a protocol on top of an established tunnel.
     *
     * @param context   the context for layering
     * @param params    the parameters for layering
     *
     * @throws IOException  in case of a problem
     */
    public void layerProtocol(final HttpContext context, final HttpParams params)
            throws IOException {

        //@@@ is context allowed to be null? depends on operator?
        Args.notNull(params, "HTTP parameters");
        Asserts.notNull(this.tracker, "Route tracker");
        Asserts.check(this.tracker.isConnected(), "Connection not open");
        Asserts.check(this.tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
        Asserts.check(!this.tracker.isLayered(), "Multiple protocol layering not supported");
        // - collect the arguments
        // - call the operator
        // - update the tracking data
        // In this order, we can be sure that only a successful
        // layering on top of the connection will be tracked.

        final HttpHost target = tracker.getTargetHost();

        connOperator.updateSecureConnection(this.connection, target,
                context, params);

        this.tracker.layerProtocol(this.connection.isSecure());

    }

    /**
     * Shuts down the entry.
     *
     * If {@link #open(HttpRoute, HttpContext, HttpParams)} is in progress,
     * this will cause that open to possibly throw an {@link IOException}.
     */
    protected void shutdownEntry() {
        tracker = null;
        state = null;
    }

}

