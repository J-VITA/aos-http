package m.vita.module.http.client.protocol;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.CredentialsProvider;
import m.vita.module.http.client.auth.AuthCache;
import m.vita.module.http.client.auth.AuthProtocolState;
import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.client.auth.AuthScope;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.client.auth.Credentials;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.RouteInfo;
import m.vita.module.http.interceptor.HttpRequestInterceptor;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HttpClientAndroidLog;

@Immutable
public class RequestAuthCache implements HttpRequestInterceptor {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    public RequestAuthCache() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        final AuthCache authCache = clientContext.getAuthCache();
        if (authCache == null) {
            this.log.debug("Auth cache not set in the context");
            return;
        }

        final CredentialsProvider credsProvider = clientContext.getCredentialsProvider();
        if (credsProvider == null) {
            this.log.debug("Credentials provider not set in the context");
            return;
        }

        final RouteInfo route = clientContext.getHttpRoute();
        if (route == null) {
            this.log.debug("Route info not set in the context");
            return;
        }

        HttpHost target = clientContext.getTargetHost();
        if (target == null) {
            this.log.debug("Target host not set in the context");
            return;
        }

        if (target.getPort() < 0) {
            target = new HttpHost(
                    target.getHostName(),
                    route.getTargetHost().getPort(),
                    target.getSchemeName());
        }

        final AuthState targetState = clientContext.getTargetAuthState();
        if (targetState != null && targetState.getState() == AuthProtocolState.UNCHALLENGED) {
            final AuthScheme authScheme = authCache.get(target);
            if (authScheme != null) {
                doPreemptiveAuth(target, authScheme, targetState, credsProvider);
            }
        }

        final HttpHost proxy = route.getProxyHost();
        final AuthState proxyState = clientContext.getProxyAuthState();
        if (proxy != null && proxyState != null && proxyState.getState() == AuthProtocolState.UNCHALLENGED) {
            final AuthScheme authScheme = authCache.get(proxy);
            if (authScheme != null) {
                doPreemptiveAuth(proxy, authScheme, proxyState, credsProvider);
            }
        }
    }

    private void doPreemptiveAuth(
            final HttpHost host,
            final AuthScheme authScheme,
            final AuthState authState,
            final CredentialsProvider credsProvider) {
        final String schemeName = authScheme.getSchemeName();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Re-using cached '" + schemeName + "' auth scheme for " + host);
        }

        final AuthScope authScope = new AuthScope(host, AuthScope.ANY_REALM, schemeName);
        final Credentials creds = credsProvider.getCredentials(authScope);

        if (creds != null) {
            if ("BASIC".equalsIgnoreCase(authScheme.getSchemeName())) {
                authState.setState(AuthProtocolState.CHALLENGED);
            } else {
                authState.setState(AuthProtocolState.SUCCESS);
            }
            authState.update(authScheme, creds);
        } else {
            this.log.debug("No credentials for preemptive authentication");
        }
    }

}
