package m.vita.module.http.connect;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.header.HttpContext;

public interface ConnectionReuseStrategy {

    /**
     * Decides whether a connection can be kept open after a request.
     * If this method returns <code>false</code>, the caller MUST
     * close the connection to correctly comply with the HTTP protocol.
     * If it returns <code>true</code>, the caller SHOULD attempt to
     * keep the connection open for reuse with another request.
     * <br/>
     * One can use the HTTP context to retrieve additional objects that
     * may be relevant for the keep-alive strategy: the actual HTTP
     * connection, the original HTTP request, target host if known,
     * number of times the connection has been reused already and so on.
     * <br/>
     * If the connection is already closed, <code>false</code> is returned.
     * The stale connection check MUST NOT be triggered by a
     * connection reuse strategy.
     *
     * @param response
     *          The last response received over that connection.
     * @param context   the context in which the connection is being
     *          used.
     *
     * @return <code>true</code> if the connection is allowed to be reused, or
     *         <code>false</code> if it MUST NOT be reused
     */
    boolean keepAlive(HttpResponse response, HttpContext context);

}
