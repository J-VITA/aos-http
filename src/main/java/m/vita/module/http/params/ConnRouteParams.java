package m.vita.module.http.params;

import java.net.InetAddress;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.util.Args;

@Immutable
public class ConnRouteParams implements ConnRoutePNames {

    /**
     * A special value indicating "no host".
     * This relies on a nonsense scheme name to avoid conflicts
     * with actual hosts. Note that this is a <i>valid</i> host.
     */
    public static final HttpHost NO_HOST =
            new HttpHost("127.0.0.255", 0, "no-host"); // Immutable

    /**
     * A special value indicating "no route".
     * This is a route with {@link #NO_HOST} as the target.
     */
    public static final HttpRoute NO_ROUTE = new HttpRoute(NO_HOST); // Immutable

    /** Disabled default constructor. */
    private ConnRouteParams() {
        // no body
    }

    /**
     * Obtains the {@link ConnRoutePNames#DEFAULT_PROXY DEFAULT_PROXY}
     * parameter value.
     * {@link #NO_HOST} will be mapped to <code>null</code>,
     * to allow unsetting in a hierarchy.
     *
     * @param params    the parameters in which to look up
     *
     * @return  the default proxy set in the argument parameters, or
     *          <code>null</code> if not set
     */
    public static HttpHost getDefaultProxy(final HttpParams params) {
        Args.notNull(params, "Parameters");
        HttpHost proxy = (HttpHost)
                params.getParameter(DEFAULT_PROXY);
        if ((proxy != null) && NO_HOST.equals(proxy)) {
            // value is explicitly unset
            proxy = null;
        }
        return proxy;
    }

    /**
     * Sets the {@link ConnRoutePNames#DEFAULT_PROXY DEFAULT_PROXY}
     * parameter value.
     *
     * @param params    the parameters in which to set the value
     * @param proxy     the value to set, may be <code>null</code>.
     *                  Note that {@link #NO_HOST} will be mapped to
     *                  <code>null</code> by {@link #getDefaultProxy},
     *                  to allow for explicit unsetting in hierarchies.
     */
    public static void setDefaultProxy(final HttpParams params,
                                       final HttpHost proxy) {
        Args.notNull(params, "Parameters");
        params.setParameter(DEFAULT_PROXY, proxy);
    }

    /**
     * Obtains the {@link ConnRoutePNames#FORCED_ROUTE FORCED_ROUTE}
     * parameter value.
     * {@link #NO_ROUTE} will be mapped to <code>null</code>,
     * to allow unsetting in a hierarchy.
     *
     * @param params    the parameters in which to look up
     *
     * @return  the forced route set in the argument parameters, or
     *          <code>null</code> if not set
     */
    public static HttpRoute getForcedRoute(final HttpParams params) {
        Args.notNull(params, "Parameters");
        HttpRoute route = (HttpRoute)
                params.getParameter(FORCED_ROUTE);
        if ((route != null) && NO_ROUTE.equals(route)) {
            // value is explicitly unset
            route = null;
        }
        return route;
    }

    /**
     * Sets the {@link ConnRoutePNames#FORCED_ROUTE FORCED_ROUTE}
     * parameter value.
     *
     * @param params    the parameters in which to set the value
     * @param route     the value to set, may be <code>null</code>.
     *                  Note that {@link #NO_ROUTE} will be mapped to
     *                  <code>null</code> by {@link #getForcedRoute},
     *                  to allow for explicit unsetting in hierarchies.
     */
    public static void setForcedRoute(final HttpParams params,
                                      final HttpRoute route) {
        Args.notNull(params, "Parameters");
        params.setParameter(FORCED_ROUTE, route);
    }

    /**
     * Obtains the {@link ConnRoutePNames#LOCAL_ADDRESS LOCAL_ADDRESS}
     * parameter value.
     * There is no special value that would automatically be mapped to
     * <code>null</code>. You can use the wildcard address (0.0.0.0 for IPv4,
     * :: for IPv6) to override a specific local address in a hierarchy.
     *
     * @param params    the parameters in which to look up
     *
     * @return  the local address set in the argument parameters, or
     *          <code>null</code> if not set
     */
    public static InetAddress getLocalAddress(final HttpParams params) {
        Args.notNull(params, "Parameters");
        final InetAddress local = (InetAddress)
                params.getParameter(LOCAL_ADDRESS);
        // no explicit unsetting
        return local;
    }

    /**
     * Sets the {@link ConnRoutePNames#LOCAL_ADDRESS LOCAL_ADDRESS}
     * parameter value.
     *
     * @param params    the parameters in which to set the value
     * @param local     the value to set, may be <code>null</code>
     */
    public static void setLocalAddress(final HttpParams params,
                                       final InetAddress local) {
        Args.notNull(params, "Parameters");
        params.setParameter(LOCAL_ADDRESS, local);
    }

}

