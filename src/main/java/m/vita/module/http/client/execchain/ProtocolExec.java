package m.vita.module.http.client.execchain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.BasicCredentialsProvider;
import m.vita.module.http.client.CredentialsProvider;
import m.vita.module.http.client.auth.AuthScope;
import m.vita.module.http.client.auth.UsernamePasswordCredentials;
import m.vita.module.http.concurrent.cancellable.HttpExecutionAware;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.execute.CloseableHttpResponse;
import m.vita.module.http.execute.HttpCoreContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.HttpUriRequest;
import m.vita.module.http.header.URIUtils;
import m.vita.module.http.method.HttpRequestWrapper;
import m.vita.module.http.params.ClientPNames;
import m.vita.module.http.client.protocol.HttpClientContext;
import m.vita.module.http.client.protocol.HttpProcessor;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HttpClientAndroidLog;

@Immutable
public class ProtocolExec implements ClientExecChain {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private final ClientExecChain requestExecutor;
    private final HttpProcessor httpProcessor;

    public ProtocolExec(final ClientExecChain requestExecutor, final HttpProcessor httpProcessor) {
        Args.notNull(requestExecutor, "HTTP client request executor");
        Args.notNull(httpProcessor, "HTTP protocol processor");
        this.requestExecutor = requestExecutor;
        this.httpProcessor = httpProcessor;
    }

    void rewriteRequestURI(
            final HttpRequestWrapper request,
            final HttpRoute route) throws ProtocolException {
        try {
            URI uri = request.getURI();
            if (uri != null) {
                if (route.getProxyHost() != null && !route.isTunnelled()) {
                    // Make sure the request URI is absolute
                    if (!uri.isAbsolute()) {
                        final HttpHost target = route.getTargetHost();
                        uri = URIUtils.rewriteURI(uri, target, true);
                    } else {
                        uri = URIUtils.rewriteURI(uri);
                    }
                } else {
                    // Make sure the request URI is relative
                    if (uri.isAbsolute()) {
                        uri = URIUtils.rewriteURI(uri, null, true);
                    } else {
                        uri = URIUtils.rewriteURI(uri);
                    }
                }
                request.setURI(uri);
            }
        } catch (final URISyntaxException ex) {
            throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
        }
    }

    public CloseableHttpResponse execute(
            final HttpRoute route,
            final HttpRequestWrapper request,
            final HttpClientContext context,
            final HttpExecutionAware execAware) throws IOException,
            HttpException {
        Args.notNull(route, "HTTP route");
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");

        final HttpRequest original = request.getOriginal();
        URI uri = null;
        if (original instanceof HttpUriRequest) {
            uri = ((HttpUriRequest) original).getURI();
        } else {
            final String uriString = original.getRequestLine().getUri();
            try {
                uri = URI.create(uriString);
            } catch (final IllegalArgumentException ex) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Unable to parse '" + uriString + "' as a valid URI; " +
                            "request URI and Host header may be inconsistent", ex);
                }
            }

        }
        request.setURI(uri);

        // Re-write request URI if needed
        rewriteRequestURI(request, route);

        final HttpParams params = request.getParams();
        HttpHost virtualHost = (HttpHost) params.getParameter(ClientPNames.VIRTUAL_HOST);
        // HTTPCLIENT-1092 - add the port if necessary
        if (virtualHost != null && virtualHost.getPort() == -1) {
            final int port = route.getTargetHost().getPort();
            if (port != -1) {
                virtualHost = new HttpHost(virtualHost.getHostName(), port,
                        virtualHost.getSchemeName());
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Using virtual host" + virtualHost);
            }
        }

        HttpHost target = null;
        if (virtualHost != null) {
            target = virtualHost;
        } else {
            if (uri != null && uri.isAbsolute() && uri.getHost() != null) {
                target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
            }
        }
        if (target == null) {
            target = route.getTargetHost();
        }

        // Get user info from the URI
        if (uri != null) {
            final String userinfo = uri.getUserInfo();
            if (userinfo != null) {
                CredentialsProvider credsProvider = context.getCredentialsProvider();
                if (credsProvider == null) {
                    credsProvider = new BasicCredentialsProvider();
                    context.setCredentialsProvider(credsProvider);
                }
                credsProvider.setCredentials(
                        new AuthScope(target),
                        new UsernamePasswordCredentials(userinfo));
            }
        }

        // Run request protocol interceptors
        context.setAttribute(HttpCoreContext.HTTP_TARGET_HOST, target);
        context.setAttribute(HttpClientContext.HTTP_ROUTE, route);
        context.setAttribute(HttpCoreContext.HTTP_REQUEST, request);

        this.httpProcessor.process(request, context);

        final CloseableHttpResponse response = this.requestExecutor.execute(route, request,
                context, execAware);
        try {
            // Run response protocol interceptors
            context.setAttribute(HttpCoreContext.HTTP_RESPONSE, response);
            this.httpProcessor.process(response, context);
            return response;
        } catch (final RuntimeException ex) {
            response.close();
            throw ex;
        } catch (final IOException ex) {
            response.close();
            throw ex;
        } catch (final HttpException ex) {
            response.close();
            throw ex;
        }
    }

}
