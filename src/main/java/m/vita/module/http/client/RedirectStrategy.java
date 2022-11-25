package m.vita.module.http.client;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpUriRequest;

public interface RedirectStrategy {

    /**
     * Determines if a request should be redirected to a new location
     * given the response from the target server.
     *
     * @param request the executed request
     * @param response the response received from the target server
     * @param context the context for the request execution
     *
     * @return <code>true</code> if the request should be redirected, <code>false</code>
     * otherwise
     */
    boolean isRedirected(
            HttpRequest request,
            HttpResponse response,
            HttpContext context) throws ProtocolException;

    /**
     * Determines the redirect location given the response from the target
     * server and the current request execution context and generates a new
     * request to be sent to the location.
     *
     * @param request the executed request
     * @param response the response received from the target server
     * @param context the context for the request execution
     *
     * @return redirected request
     */
    HttpUriRequest getRedirect(
            HttpRequest request,
            HttpResponse response,
            HttpContext context) throws ProtocolException;

}
