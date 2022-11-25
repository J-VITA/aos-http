package m.vita.module.http.client.execchain;

import java.io.IOException;

import m.vita.module.http.concurrent.cancellable.HttpExecutionAware;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.execute.CloseableHttpResponse;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.method.HttpRequestWrapper;
import m.vita.module.http.client.protocol.HttpClientContext;

public interface ClientExecChain {

    /**
     * Executes th request either by transmitting it to the target server or
     * by passing it onto the next executor in the request execution chain.
     *
     * @param route connection route.
     * @param request current request.
     * @param clientContext current HTTP context.
     * @param execAware receiver of notifications of blocking I/O operations.
     * @return HTTP response either received from the opposite endpoint
     *   or generated locally.
     * @throws IOException in case of a I/O error.
     *   (this type of exceptions are potentially recoverable).
     * @throws HttpException in case of an HTTP protocol error
     *   (usually this type of exceptions are non-recoverable).
     */
    CloseableHttpResponse execute(
            HttpRoute route,
            HttpRequestWrapper request,
            HttpClientContext clientContext,
            HttpExecutionAware execAware) throws IOException, HttpException;

}
