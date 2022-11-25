package m.vita.module.http.connect;

import java.net.InetAddress;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.Scheme;
import m.vita.module.http.connect.route.HttpRoutePlanner;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.factory.SchemeRegistry;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.params.ConnRouteParams;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.Asserts;

@ThreadSafe
public class DefaultHttpRoutePlanner implements HttpRoutePlanner {

    /** The scheme registry. */
    protected final SchemeRegistry schemeRegistry; // class is @ThreadSafe

    /**
     * Creates a new default route planner.
     *
     * @param schreg    the scheme registry
     */
    public DefaultHttpRoutePlanner(final SchemeRegistry schreg) {
        Args.notNull(schreg, "Scheme registry");
        schemeRegistry = schreg;
    }

    public HttpRoute determineRoute(final HttpHost target,
                                    final HttpRequest request,
                                    final HttpContext context)
            throws HttpException {

        Args.notNull(request, "HTTP request");

        // If we have a forced route, we can do without a target.
        HttpRoute route =
                ConnRouteParams.getForcedRoute(request.getParams());
        if (route != null) {
            return route;
        }

        // If we get here, there is no forced route.
        // So we need a target to compute a route.

        Asserts.notNull(target, "Target host");

        final InetAddress local =
                ConnRouteParams.getLocalAddress(request.getParams());
        final HttpHost proxy =
                ConnRouteParams.getDefaultProxy(request.getParams());

        final Scheme schm;
        try {
            schm = this.schemeRegistry.getScheme(target.getSchemeName());
        } catch (final IllegalStateException ex) {
            throw new HttpException(ex.getMessage());
        }
        // as it is typically used for TLS/SSL, we assume that
        // a layered scheme implies a secure connection
        final boolean secure = schm.isLayered();

        if (proxy == null) {
            route = new HttpRoute(target, local, secure);
        } else {
            route = new HttpRoute(target, local, proxy, secure);
        }
        return route;
    }

}
