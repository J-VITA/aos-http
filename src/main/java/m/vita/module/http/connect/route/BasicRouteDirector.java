package m.vita.module.http.connect.route;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.header.RouteInfo;
import m.vita.module.http.util.Args;

@Immutable
public class BasicRouteDirector implements HttpRouteDirector {

    /**
     * Provides the next step.
     *
     * @param plan      the planned route
     * @param fact      the currently established route, or
     *                  <code>null</code> if nothing is established
     *
     * @return  one of the constants defined in this class, indicating
     *          either the next step to perform, or success, or failure.
     *          0 is for success, a negative value for failure.
     */
    public int nextStep(final RouteInfo plan, final RouteInfo fact) {
        Args.notNull(plan, "Planned route");

        int step = UNREACHABLE;

        if ((fact == null) || (fact.getHopCount() < 1)) {
            step = firstStep(plan);
        } else if (plan.getHopCount() > 1) {
            step = proxiedStep(plan, fact);
        } else {
            step = directStep(plan, fact);
        }

        return step;

    } // nextStep


    /**
     * Determines the first step to establish a route.
     *
     * @param plan      the planned route
     *
     * @return  the first step
     */
    protected int firstStep(final RouteInfo plan) {

        return (plan.getHopCount() > 1) ?
                CONNECT_PROXY : CONNECT_TARGET;
    }


    /**
     * Determines the next step to establish a direct connection.
     *
     * @param plan      the planned route
     * @param fact      the currently established route
     *
     * @return  one of the constants defined in this class, indicating
     *          either the next step to perform, or success, or failure
     */
    protected int directStep(final RouteInfo plan, final RouteInfo fact) {

        if (fact.getHopCount() > 1) {
            return UNREACHABLE;
        }
        if (!plan.getTargetHost().equals(fact.getTargetHost()))
        {
            return UNREACHABLE;
            // If the security is too low, we could now suggest to layer
            // a secure protocol on the direct connection. Layering on direct
            // connections has not been supported in HttpClient 3.x, we don't
            // consider it here until there is a real-life use case for it.
        }

        // Should we tolerate if security is better than planned?
        // (plan.isSecure() && !fact.isSecure())
        if (plan.isSecure() != fact.isSecure()) {
            return UNREACHABLE;
        }

        // Local address has to match only if the plan specifies one.
        if ((plan.getLocalAddress() != null) &&
                !plan.getLocalAddress().equals(fact.getLocalAddress())
        ) {
            return UNREACHABLE;
        }

        return COMPLETE;
    }


    /**
     * Determines the next step to establish a connection via proxy.
     *
     * @param plan      the planned route
     * @param fact      the currently established route
     *
     * @return  one of the constants defined in this class, indicating
     *          either the next step to perform, or success, or failure
     */
    protected int proxiedStep(final RouteInfo plan, final RouteInfo fact) {

        if (fact.getHopCount() <= 1) {
            return UNREACHABLE;
        }
        if (!plan.getTargetHost().equals(fact.getTargetHost())) {
            return UNREACHABLE;
        }
        final int phc = plan.getHopCount();
        final int fhc = fact.getHopCount();
        if (phc < fhc) {
            return UNREACHABLE;
        }

        for (int i=0; i<fhc-1; i++) {
            if (!plan.getHopTarget(i).equals(fact.getHopTarget(i))) {
                return UNREACHABLE;
            }
        }
        // now we know that the target matches and proxies so far are the same
        if (phc > fhc)
        {
            return TUNNEL_PROXY; // need to extend the proxy chain
        }

        // proxy chain and target are the same, check tunnelling and layering
        if ((fact.isTunnelled() && !plan.isTunnelled()) ||
                (fact.isLayered()   && !plan.isLayered())) {
            return UNREACHABLE;
        }

        if (plan.isTunnelled() && !fact.isTunnelled()) {
            return TUNNEL_TARGET;
        }
        if (plan.isLayered() && !fact.isLayered()) {
            return LAYER_PROTOCOL;
        }

        // tunnel and layering are the same, remains to check the security
        // Should we tolerate if security is better than planned?
        // (plan.isSecure() && !fact.isSecure())
        if (plan.isSecure() != fact.isSecure()) {
            return UNREACHABLE;
        }

        return COMPLETE;
    }

}
