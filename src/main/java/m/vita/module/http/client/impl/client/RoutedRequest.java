package m.vita.module.http.client.impl.client;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.HttpRoute;

@NotThreadSafe // RequestWrapper is @NotThreadSafe
public class RoutedRequest {

    protected final RequestWrapper request; // @NotThreadSafe
    protected final HttpRoute route; // @Immutable

    /**
     * Creates a new routed request.
     *
     * @param req   the request
     * @param route   the route
     */
    public RoutedRequest(final RequestWrapper req, final HttpRoute route) {
        super();
        this.request = req;
        this.route   = route;
    }

    public final RequestWrapper getRequest() {
        return request;
    }

    public final HttpRoute getRoute() {
        return route;
    }

}
