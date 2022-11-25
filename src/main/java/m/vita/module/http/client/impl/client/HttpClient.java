package m.vita.module.http.client.impl.client;

import java.io.IOException;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.config.RequestConfig;
import m.vita.module.http.exception.ClientProtocolException;
import m.vita.module.http.handler.ResponseHandler;
import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpUriRequest;

@SuppressWarnings("deprecation")
public interface HttpClient {


    /**
     * Obtains the parameters for this client.
     * These parameters will become defaults for all requests being
     * executed with this client, and for the parameters of
     * dependent objects in this client.
     *
     * @return  the default parameters
     *
     * @deprecated (4.3) use
     *   {@link RequestConfig}.
     */
    @Deprecated
    HttpParams getParams();

    /**
     * Obtains the connection manager used by this client.
     *
     * @return  the connection manager
     *
     * @deprecated (4.3) use
     *   {@link HttpClientBuilder}.
     */
    @Deprecated
    ClientConnectionManager getConnectionManager();

    /**
     * Executes HTTP request using the default context.
     *
     * @param request   the request to execute
     *
     * @return  the response to the request. This is always a final response,
     *          never an intermediate response with an 1xx status code.
     *          Whether redirects or authentication challenges will be returned
     *          or handled automatically depends on the implementation and
     *          configuration of this client.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    HttpResponse execute(HttpUriRequest request)
            throws IOException, ClientProtocolException;

    /**
     * Executes HTTP request using the given context.
     *
     * @param request   the request to execute
     * @param context   the context to use for the execution, or
     *                  <code>null</code> to use the default context
     *
     * @return  the response to the request. This is always a final response,
     *          never an intermediate response with an 1xx status code.
     *          Whether redirects or authentication challenges will be returned
     *          or handled automatically depends on the implementation and
     *          configuration of this client.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    HttpResponse execute(HttpUriRequest request, HttpContext context)
            throws IOException, ClientProtocolException;

    /**
     * Executes HTTP request using the default context.
     *
     * @param target    the target host for the request.
     *                  Implementations may accept <code>null</code>
     *                  if they can still determine a route, for example
     *                  to a default target or by inspecting the request.
     * @param request   the request to execute
     *
     * @return  the response to the request. This is always a final response,
     *          never an intermediate response with an 1xx status code.
     *          Whether redirects or authentication challenges will be returned
     *          or handled automatically depends on the implementation and
     *          configuration of this client.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    HttpResponse execute(HttpHost target, HttpRequest request)
            throws IOException, ClientProtocolException;

    /**
     * Executes HTTP request using the given context.
     *
     * @param target    the target host for the request.
     *                  Implementations may accept <code>null</code>
     *                  if they can still determine a route, for example
     *                  to a default target or by inspecting the request.
     * @param request   the request to execute
     * @param context   the context to use for the execution, or
     *                  <code>null</code> to use the default context
     *
     * @return  the response to the request. This is always a final response,
     *          never an intermediate response with an 1xx status code.
     *          Whether redirects or authentication challenges will be returned
     *          or handled automatically depends on the implementation and
     *          configuration of this client.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    HttpResponse execute(HttpHost target, HttpRequest request,
                         HttpContext context)
            throws IOException, ClientProtocolException;

    /**
     * Executes HTTP request using the default context and processes the
     * response using the given response handler.
     * <p/>
     * Implementing classes are required to ensure that the content entity
     * associated with the response is fully consumed and the underlying
     * connection is released back to the connection manager automatically
     * in all cases relieving individual {@link ResponseHandler}s from
     * having to manage resource deallocation internally.
     *
     * @param request   the request to execute
     * @param responseHandler the response handler
     *
     * @return  the response object as generated by the response handler.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    <T> T execute(
            HttpUriRequest request,
            ResponseHandler<? extends T> responseHandler)
            throws IOException, ClientProtocolException;

    /**
     * Executes HTTP request using the given context and processes the
     * response using the given response handler.
     * <p/>
     * Implementing classes are required to ensure that the content entity
     * associated with the response is fully consumed and the underlying
     * connection is released back to the connection manager automatically
     * in all cases relieving individual {@link ResponseHandler}s from
     * having to manage resource deallocation internally.
     *
     * @param request   the request to execute
     * @param responseHandler the response handler
     * @param context   the context to use for the execution, or
     *                  <code>null</code> to use the default context
     *
     * @return  the response object as generated by the response handler.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    <T> T execute(
            HttpUriRequest request,
            ResponseHandler<? extends T> responseHandler,
            HttpContext context)
            throws IOException, ClientProtocolException;

    /**
     * Executes HTTP request to the target using the default context and
     * processes the response using the given response handler.
     * <p/>
     * Implementing classes are required to ensure that the content entity
     * associated with the response is fully consumed and the underlying
     * connection is released back to the connection manager automatically
     * in all cases relieving individual {@link ResponseHandler}s from
     * having to manage resource deallocation internally.
     *
     * @param target    the target host for the request.
     *                  Implementations may accept <code>null</code>
     *                  if they can still determine a route, for example
     *                  to a default target or by inspecting the request.
     * @param request   the request to execute
     * @param responseHandler the response handler
     *
     * @return  the response object as generated by the response handler.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    <T> T execute(
            HttpHost target,
            HttpRequest request,
            ResponseHandler<? extends T> responseHandler)
            throws IOException, ClientProtocolException;

    /**
     * Executes HTTP request to the target using the given context and
     * processes the response using the given response handler.
     * <p/>
     * Implementing classes are required to ensure that the content entity
     * associated with the response is fully consumed and the underlying
     * connection is released back to the connection manager automatically
     * in all cases relieving individual {@link ResponseHandler}s from
     * having to manage resource deallocation internally.
     *
     * @param target    the target host for the request.
     *                  Implementations may accept <code>null</code>
     *                  if they can still determine a route, for example
     *                  to a default target or by inspecting the request.
     * @param request   the request to execute
     * @param responseHandler the response handler
     * @param context   the context to use for the execution, or
     *                  <code>null</code> to use the default context
     *
     * @return  the response object as generated by the response handler.
     * @throws IOException in case of a problem or the connection was aborted
     * @throws ClientProtocolException in case of an http protocol error
     */
    <T> T execute(
            HttpHost target,
            HttpRequest request,
            ResponseHandler<? extends T> responseHandler,
            HttpContext context)
            throws IOException, ClientProtocolException;

}
