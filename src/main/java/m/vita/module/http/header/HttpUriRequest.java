package m.vita.module.http.header;

import java.net.URI;

public interface HttpUriRequest extends HttpRequest {

    /**
     * Returns the HTTP method this request uses, such as <code>GET</code>,
     * <code>PUT</code>, <code>POST</code>, or other.
     */
    String getMethod();

    /**
     * Returns the URI this request uses, such as
     * <code>http://example.org/path/to/file</code>.
     * <br/>
     * Note that the URI may be absolute URI (as above) or may be a relative URI.
     * <p>
     * Implementations are encouraged to return
     * the URI that was initially requested.
     * </p>
     * <p>
     * To find the final URI after any redirects have been processed,
     * please see the section entitled
     * <a href="http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d4e205">HTTP execution context</a>
     * in the
     * <a href="http://hc.apache.org/httpcomponents-client-ga/tutorial/html">HttpClient Tutorial</a>
     * </p>
     */
    URI getURI();

    /**
     * Aborts execution of the request.
     *
     * @throws UnsupportedOperationException if the abort operation
     *   is not supported / cannot be implemented.
     */
    void abort() throws UnsupportedOperationException;

    /**
     * Tests if the request execution has been aborted.
     *
     * @return <code>true</code> if the request execution has been aborted,
     *   <code>false</code> otherwise.
     */
    boolean isAborted();

}
