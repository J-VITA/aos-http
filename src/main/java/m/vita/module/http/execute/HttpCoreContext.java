package m.vita.module.http.execute;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.connect.HttpConnection;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class HttpCoreContext implements HttpContext {

    /**
     * Attribute name of a {@link HttpConnection} object that
     * represents the actual HTTP connection.
     */
    public static final String HTTP_CONNECTION  = "http.connection";

    /**
     * Attribute name of a {@link HttpRequest} object that
     * represents the actual HTTP request.
     */
    public static final String HTTP_REQUEST     = "http.request";

    /**
     * Attribute name of a {@link HttpResponse} object that
     * represents the actual HTTP response.
     */
    public static final String HTTP_RESPONSE    = "http.response";

    /**
     * Attribute name of a {@link HttpHost} object that
     * represents the connection target.
     */
    public static final String HTTP_TARGET_HOST = "http.target_host";

    /**
     * Attribute name of a {@link Boolean} object that represents the
     * the flag indicating whether the actual request has been fully transmitted
     * to the target host.
     */
    public static final String HTTP_REQ_SENT    = "http.request_sent";

    public static HttpCoreContext create() {
        return new HttpCoreContext(new BasicHttpContext());
    }

    public static HttpCoreContext adapt(final HttpContext context) {
        Args.notNull(context, "HTTP context");
        if (context instanceof HttpCoreContext) {
            return (HttpCoreContext) context;
        } else {
            return new HttpCoreContext(context);
        }
    }

    private final HttpContext context;

    public HttpCoreContext(final HttpContext context) {
        super();
        this.context = context;
    }

    public HttpCoreContext() {
        super();
        this.context = new BasicHttpContext();
    }

    public Object getAttribute(final String id) {
        return context.getAttribute(id);
    }

    public void setAttribute(final String id, final Object obj) {
        context.setAttribute(id, obj);
    }

    public Object removeAttribute(final String id) {
        return context.removeAttribute(id);
    }

    public <T> T getAttribute(final String attribname, final Class<T> clazz) {
        Args.notNull(clazz, "Attribute class");
        final Object obj = getAttribute(attribname);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }

    public <T extends HttpConnection> T getConnection(final Class<T> clazz) {
        return getAttribute(HTTP_CONNECTION, clazz);
    }

    public HttpConnection getConnection() {
        return getAttribute(HTTP_CONNECTION, HttpConnection.class);
    }

    public HttpRequest getRequest() {
        return getAttribute(HTTP_REQUEST, HttpRequest.class);
    }

    public boolean isRequestSent() {
        final Boolean b = getAttribute(HTTP_REQ_SENT, Boolean.class);
        return b != null && b.booleanValue();
    }

    public HttpResponse getResponse() {
        return getAttribute(HTTP_RESPONSE, HttpResponse.class);
    }

    public void setTargetHost(final HttpHost host) {
        setAttribute(HTTP_TARGET_HOST, host);
    }

    public HttpHost getTargetHost() {
        return getAttribute(HTTP_TARGET_HOST, HttpHost.class);
    }

}
