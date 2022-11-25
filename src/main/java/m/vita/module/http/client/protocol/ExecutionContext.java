package m.vita.module.http.client.protocol;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.connect.HttpConnection;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;

public interface ExecutionContext {

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
     * Attribute name of a {@link HttpHost} object that
     * represents the connection proxy.
     *
     * @deprecated (4.3) do not use.
     */
    @Deprecated
    public static final String HTTP_PROXY_HOST  = "http.proxy_host";

    /**
     * Attribute name of a {@link Boolean} object that represents the
     * the flag indicating whether the actual request has been fully transmitted
     * to the target host.
     */
    public static final String HTTP_REQ_SENT    = "http.request_sent";

}
