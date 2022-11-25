package m.vita.module.http.client.execchain;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.RedirectStrategy;
import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.concurrent.cancellable.HttpExecutionAware;
import m.vita.module.http.config.RequestConfig;
import m.vita.module.http.connect.route.HttpRoutePlanner;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.exception.RedirectException;
import m.vita.module.http.execute.CloseableHttpResponse;
import m.vita.module.http.header.HttpEntityEnclosingRequest;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.URIUtils;
import m.vita.module.http.method.HttpRequestWrapper;
import m.vita.module.http.client.protocol.HttpClientContext;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.EntityUtils;
import m.vita.module.http.util.HttpClientAndroidLog;

@ThreadSafe
public class RedirectExec implements ClientExecChain {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private final ClientExecChain requestExecutor;
    private final RedirectStrategy redirectStrategy;
    private final HttpRoutePlanner routePlanner;

    public RedirectExec(
            final ClientExecChain requestExecutor,
            final HttpRoutePlanner routePlanner,
            final RedirectStrategy redirectStrategy) {
        super();
        Args.notNull(requestExecutor, "HTTP client request executor");
        Args.notNull(routePlanner, "HTTP route planner");
        Args.notNull(redirectStrategy, "HTTP redirect strategy");
        this.requestExecutor = requestExecutor;
        this.routePlanner = routePlanner;
        this.redirectStrategy = redirectStrategy;
    }

    public CloseableHttpResponse execute(
            final HttpRoute route,
            final HttpRequestWrapper request,
            final HttpClientContext context,
            final HttpExecutionAware execAware) throws IOException, HttpException {
        Args.notNull(route, "HTTP route");
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");

        final List<URI> redirectLocations = context.getRedirectLocations();
        if (redirectLocations != null) {
            redirectLocations.clear();
        }

        final RequestConfig config = context.getRequestConfig();
        final int maxRedirects = config.getMaxRedirects() > 0 ? config.getMaxRedirects() : 50;
        HttpRoute currentRoute = route;
        HttpRequestWrapper currentRequest = request;
        for (int redirectCount = 0;;) {
            final CloseableHttpResponse response = requestExecutor.execute(
                    currentRoute, currentRequest, context, execAware);
            try {
                if (config.isRedirectsEnabled() &&
                        this.redirectStrategy.isRedirected(currentRequest, response, context)) {

                    if (redirectCount >= maxRedirects) {
                        throw new RedirectException("Maximum redirects ("+ maxRedirects + ") exceeded");
                    }
                    redirectCount++;

                    final HttpRequest redirect = this.redirectStrategy.getRedirect(
                            currentRequest, response, context);
                    if (!redirect.headerIterator().hasNext()) {
                        final HttpRequest original = request.getOriginal();
                        redirect.setHeaders(original.getAllHeaders());
                    }
                    currentRequest = HttpRequestWrapper.wrap(redirect);

                    if (currentRequest instanceof HttpEntityEnclosingRequest) {
                        RequestEntityProxy.enhance((HttpEntityEnclosingRequest) currentRequest);
                    }

                    final URI uri = currentRequest.getURI();
                    final HttpHost newTarget = URIUtils.extractHost(uri);
                    if (newTarget == null) {
                        throw new ProtocolException("Redirect URI does not specify a valid host name: " +
                                uri);
                    }

                    // Reset virtual host and auth states if redirecting to another host
                    if (!currentRoute.getTargetHost().equals(newTarget)) {
                        final AuthState targetAuthState = context.getTargetAuthState();
                        if (targetAuthState != null) {
                            this.log.debug("Resetting target auth state");
                            targetAuthState.reset();
                        }
                        final AuthState proxyAuthState = context.getProxyAuthState();
                        if (proxyAuthState != null) {
                            final AuthScheme authScheme = proxyAuthState.getAuthScheme();
                            if (authScheme != null && authScheme.isConnectionBased()) {
                                this.log.debug("Resetting proxy auth state");
                                proxyAuthState.reset();
                            }
                        }
                    }

                    currentRoute = this.routePlanner.determineRoute(newTarget, currentRequest, context);
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Redirecting to '" + uri + "' via " + currentRoute);
                    }
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } else {
                    return response;
                }
            } catch (final RuntimeException ex) {
                response.close();
                throw ex;
            } catch (final IOException ex) {
                response.close();
                throw ex;
            } catch (final HttpException ex) {
                // Protocol exception related to a direct.
                // The underlying connection may still be salvaged.
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (final IOException ioex) {
                    this.log.debug("I/O error while releasing connection", ioex);
                } finally {
                    response.close();
                }
                throw ex;
            }
        }
    }

}
