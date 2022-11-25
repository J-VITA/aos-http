package m.vita.module.http.params;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.util.Args;

@ThreadSafe
public final class ConnPerRouteBean implements ConnPerRoute {

    /** The default maximum number of connections allowed per host */
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;   // Per RFC 2616 sec 8.1.4

    private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap;

    private volatile int defaultMax;

    public ConnPerRouteBean(final int defaultMax) {
        super();
        this.maxPerHostMap = new ConcurrentHashMap<HttpRoute, Integer>();
        setDefaultMaxPerRoute(defaultMax);
    }

    public ConnPerRouteBean() {
        this(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
    }

    public int getDefaultMax() {
        return this.defaultMax;
    }

    /**
     * @since 4.1
     */
    public int getDefaultMaxPerRoute() {
        return this.defaultMax;
    }

    public void setDefaultMaxPerRoute(final int max) {
        Args.positive(max, "Defautl max per route");
        this.defaultMax = max;
    }

    public void setMaxForRoute(final HttpRoute route, final int max) {
        Args.notNull(route, "HTTP route");
        Args.positive(max, "Max per route");
        this.maxPerHostMap.put(route, Integer.valueOf(max));
    }

    public int getMaxForRoute(final HttpRoute route) {
        Args.notNull(route, "HTTP route");
        final Integer max = this.maxPerHostMap.get(route);
        if (max != null) {
            return max.intValue();
        } else {
            return this.defaultMax;
        }
    }

    public void setMaxForRoutes(final Map<HttpRoute, Integer> map) {
        if (map == null) {
            return;
        }
        this.maxPerHostMap.clear();
        this.maxPerHostMap.putAll(map);
    }

    @Override
    public String toString() {
        return this.maxPerHostMap.toString();
    }

}
