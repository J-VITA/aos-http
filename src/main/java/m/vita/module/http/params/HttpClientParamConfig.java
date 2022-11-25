package m.vita.module.http.params;

import java.net.InetAddress;
import java.util.Collection;

import m.vita.module.http.client.auth.params.AuthPNames;
import m.vita.module.http.config.RequestConfig;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpParams;

public final class HttpClientParamConfig {

    private HttpClientParamConfig() {
    }

    @SuppressWarnings("unchecked")
    public static RequestConfig getRequestConfig(final HttpParams params) {
        return RequestConfig.custom()
                .setSocketTimeout(params.getIntParameter(
                        CoreConnectionPNames.SO_TIMEOUT, 0))
                .setStaleConnectionCheckEnabled(params.getBooleanParameter(
                        CoreConnectionPNames.STALE_CONNECTION_CHECK, true))
                .setConnectTimeout(params.getIntParameter(
                        CoreConnectionPNames.CONNECTION_TIMEOUT, 0))
                .setExpectContinueEnabled(params.getBooleanParameter(
                        CoreProtocolPNames.USE_EXPECT_CONTINUE, false))
                .setProxy((HttpHost) params.getParameter(
                        ConnRoutePNames.DEFAULT_PROXY))
                .setLocalAddress((InetAddress) params.getParameter(
                        ConnRoutePNames.LOCAL_ADDRESS))
                .setProxyPreferredAuthSchemes((Collection<String>) params.getParameter(
                        AuthPNames.PROXY_AUTH_PREF))
                .setTargetPreferredAuthSchemes((Collection<String>) params.getParameter(
                        AuthPNames.TARGET_AUTH_PREF))
                .setAuthenticationEnabled(params.getBooleanParameter(
                        ClientPNames.HANDLE_AUTHENTICATION, true))
                .setCircularRedirectsAllowed(params.getBooleanParameter(
                        ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false))
                .setConnectionRequestTimeout((int) params.getLongParameter(
                        ClientPNames.CONN_MANAGER_TIMEOUT, 0))
                .setCookieSpec((String) params.getParameter(
                        ClientPNames.COOKIE_POLICY))
                .setMaxRedirects(params.getIntParameter(
                        ClientPNames.MAX_REDIRECTS, 50))
                .setRedirectsEnabled(params.getBooleanParameter(
                        ClientPNames.HANDLE_REDIRECTS, true))
                .setRelativeRedirectsAllowed(!params.getBooleanParameter(
                        ClientPNames.REJECT_RELATIVE_REDIRECT, false))
                .build();
    }

}
