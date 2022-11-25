package m.vita.module.http.client.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.ServiceUnavailableRetryStrategy;
import m.vita.module.http.concurrent.cancellable.HttpExecutionAware;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.execute.CloseableHttpResponse;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.method.HttpRequestWrapper;
import m.vita.module.http.client.protocol.HttpClientContext;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HttpClientAndroidLog;

@Immutable
public class ServiceUnavailableRetryExec implements ClientExecChain {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private final ClientExecChain requestExecutor;
    private final ServiceUnavailableRetryStrategy retryStrategy;

    public ServiceUnavailableRetryExec(
            final ClientExecChain requestExecutor,
            final ServiceUnavailableRetryStrategy retryStrategy) {
        super();
        Args.notNull(requestExecutor, "HTTP request executor");
        Args.notNull(retryStrategy, "Retry strategy");
        this.requestExecutor = requestExecutor;
        this.retryStrategy = retryStrategy;
    }

    public CloseableHttpResponse execute(
            final HttpRoute route,
            final HttpRequestWrapper request,
            final HttpClientContext context,
            final HttpExecutionAware execAware) throws IOException, HttpException {
        final Header[] origheaders = request.getAllHeaders();
        for (int c = 1;; c++) {
            final CloseableHttpResponse response = this.requestExecutor.execute(
                    route, request, context, execAware);
            try {
                if (this.retryStrategy.retryRequest(response, c, context)) {
                    response.close();
                    final long nextInterval = this.retryStrategy.getRetryInterval();
                    if (nextInterval > 0) {
                        try {
                            this.log.trace("Wait for " + nextInterval);
                            Thread.sleep(nextInterval);
                        } catch (final InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new InterruptedIOException();
                        }
                    }
                    request.setHeaders(origheaders);
                } else {
                    return response;
                }
            } catch (final RuntimeException ex) {
                response.close();
                throw ex;
            }
        }
    }

}
