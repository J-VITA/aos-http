package m.vita.module.http.connect;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.header.HttpContext;

public interface ConnectionKeepAliveStrategy {

    /**
     * Returns the duration of time which this connection can be safely kept
     * idle. If the connection is left idle for longer than this period of time,
     * it MUST not reused. A value of 0 or less may be returned to indicate that
     * there is no suitable suggestion.
     *
     * When coupled with a {@link ConnectionReuseStrategy}, if
     * {@link ConnectionReuseStrategy#keepAlive(
     *   HttpResponse, HttpContext)} returns true, this allows you to control
     * how long the reuse will last. If keepAlive returns false, this should
     * have no meaningful impact
     *
     * @param response
     *            The last response received over the connection.
     * @param context
     *            the context in which the connection is being used.
     *
     * @return the duration in ms for which it is safe to keep the connection
     *         idle, or <=0 if no suggested duration.
     */
    long getKeepAliveDuration(HttpResponse response, HttpContext context);

}
