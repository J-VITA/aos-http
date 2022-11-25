package m.vita.module.http.connect.route;

import m.vita.module.http.header.RouteInfo;

public interface HttpRouteDirector {

    /** Indicates that the route can not be established at all. */
    public final static int UNREACHABLE = -1;

    /** Indicates that the route is complete. */
    public final static int COMPLETE = 0;

    /** Step: open connection to target. */
    public final static int CONNECT_TARGET = 1;

    /** Step: open connection to proxy. */
    public final static int CONNECT_PROXY = 2;

    /** Step: tunnel through proxy to target. */
    public final static int TUNNEL_TARGET = 3;

    /** Step: tunnel through proxy to other proxy. */
    public final static int TUNNEL_PROXY = 4;

    /** Step: layer protocol (over tunnel). */
    public final static int LAYER_PROTOCOL = 5;


    /**
     * Provides the next step.
     *
     * @param plan      the planned route
     * @param fact      the currently established route, or
     *                  <code>null</code> if nothing is established
     *
     * @return  one of the constants defined in this interface, indicating
     *          either the next step to perform, or success, or failure.
     *          0 is for success, a negative value for failure.
     */
    public int nextStep(RouteInfo plan, RouteInfo fact);

}
