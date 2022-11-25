package m.vita.module.http.client;

import java.net.URI;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.header.HttpContext;

public interface RedirectHandler {

    /**
     * Determines if a request should be redirected to a new location
     * given the response from the target server.
     *
     * @param response the response received from the target server
     * @param context the context for the request execution
     *
     * @return <code>true</code> if the request should be redirected, <code>false</code>
     * otherwise
     */
    boolean isRedirectRequested(HttpResponse response, HttpContext context);

    /**
     * Determines the location request is expected to be redirected to
     * given the response from the target server and the current request
     * execution context.
     *
     * @param response the response received from the target server
     * @param context the context for the request execution
     *
     * @return redirect URI
     */
    URI getLocationURI(HttpResponse response, HttpContext context)
            throws ProtocolException;

}
