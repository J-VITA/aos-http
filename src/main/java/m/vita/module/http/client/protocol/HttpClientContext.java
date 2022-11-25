package m.vita.module.http.client.protocol;

import java.net.URI;
import java.util.List;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.client.CredentialsProvider;
import m.vita.module.http.client.auth.AuthCache;
import m.vita.module.http.client.auth.AuthSchemeProvider;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.config.Lookup;
import m.vita.module.http.config.RequestConfig;
import m.vita.module.http.cookie.CookieOrigin;
import m.vita.module.http.cookie.CookieSpec;
import m.vita.module.http.cookie.CookieSpecProvider;
import m.vita.module.http.cookie.CookieStore;
import m.vita.module.http.execute.BasicHttpContext;
import m.vita.module.http.execute.HttpCoreContext;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.RouteInfo;

@NotThreadSafe
public class HttpClientContext extends HttpCoreContext {

    /**
     * Attribute name of a {@link RouteInfo}
     * object that represents the actual connection route.
     */
    public static final String HTTP_ROUTE   = "http.route";

    /**
     * Attribute name of a {@link List} object that represents a collection of all
     * redirect locations received in the process of request execution.
     */
    public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";

    /**
     * Attribute name of a {@link Lookup} object that represents
     * the actual {@link CookieSpecProvider} registry.
     */
    public static final String COOKIESPEC_REGISTRY   = "http.cookiespec-registry";

    /**
     * Attribute name of a {@link CookieSpec}
     * object that represents the actual cookie specification.
     */
    public static final String COOKIE_SPEC           = "http.cookie-spec";

    /**
     * Attribute name of a {@link CookieOrigin}
     * object that represents the actual details of the origin server.
     */
    public static final String COOKIE_ORIGIN         = "http.cookie-origin";

    /**
     * Attribute name of a {@link CookieStore}
     * object that represents the actual cookie store.
     */
    public static final String COOKIE_STORE          = "http.cookie-store";

    /**
     * Attribute name of a {@link CredentialsProvider}
     * object that represents the actual credentials provider.
     */
    public static final String CREDS_PROVIDER        = "http.auth.credentials-provider";

    /**
     * Attribute name of a {@link AuthCache} object
     * that represents the auth scheme cache.
     */
    public static final String AUTH_CACHE            = "http.auth.auth-cache";

    /**
     * Attribute name of a {@link AuthState}
     * object that represents the actual target authentication state.
     */
    public static final String TARGET_AUTH_STATE     = "http.auth.target-scope";

    /**
     * Attribute name of a {@link AuthState}
     * object that represents the actual proxy authentication state.
     */
    public static final String PROXY_AUTH_STATE      = "http.auth.proxy-scope";

    /**
     * Attribute name of a {@link Object} object that represents
     * the actual user identity such as user {@link java.security.Principal}.
     */
    public static final String USER_TOKEN            = "http.user-token";

    /**
     * Attribute name of a {@link Lookup} object that represents
     * the actual {@link AuthSchemeProvider} registry.
     */
    public static final String AUTHSCHEME_REGISTRY   = "http.authscheme-registry";

    /**
     * Attribute name of a {@link RequestConfig} object that
     * represents the actual request configuration.
     */
    public static final String REQUEST_CONFIG = "http.request-config";

    public static HttpClientContext adapt(final HttpContext context) {
        if (context instanceof HttpClientContext) {
            return (HttpClientContext) context;
        } else {
            return new HttpClientContext(context);
        }
    }

    public static HttpClientContext create() {
        return new HttpClientContext(new BasicHttpContext());
    }

    public HttpClientContext(final HttpContext context) {
        super(context);
    }

    public HttpClientContext() {
        super();
    }

    public RouteInfo getHttpRoute() {
        return getAttribute(HTTP_ROUTE, HttpRoute.class);
    }

    @SuppressWarnings("unchecked")
    public List<URI> getRedirectLocations() {
        return getAttribute(REDIRECT_LOCATIONS, List.class);
    }

    public CookieStore getCookieStore() {
        return getAttribute(COOKIE_STORE, CookieStore.class);
    }

    public void setCookieStore(final CookieStore cookieStore) {
        setAttribute(COOKIE_STORE, cookieStore);
    }

    public CookieSpec getCookieSpec() {
        return getAttribute(COOKIE_SPEC, CookieSpec.class);
    }

    public CookieOrigin getCookieOrigin() {
        return getAttribute(COOKIE_ORIGIN, CookieOrigin.class);
    }

    @SuppressWarnings("unchecked")
    private <T> Lookup<T> getLookup(final String name, final Class<T> clazz) {
        return getAttribute(name, Lookup.class);
    }

    public Lookup<CookieSpecProvider> getCookieSpecRegistry() {
        return getLookup(COOKIESPEC_REGISTRY, CookieSpecProvider.class);
    }

    public void setCookieSpecRegistry(final Lookup<CookieSpecProvider> lookup) {
        setAttribute(COOKIESPEC_REGISTRY, lookup);
    }

    public Lookup<AuthSchemeProvider> getAuthSchemeRegistry() {
        return getLookup(AUTHSCHEME_REGISTRY, AuthSchemeProvider.class);
    }

    public void setAuthSchemeRegistry(final Lookup<AuthSchemeProvider> lookup) {
        setAttribute(AUTHSCHEME_REGISTRY, lookup);
    }

    public CredentialsProvider getCredentialsProvider() {
        return getAttribute(CREDS_PROVIDER, CredentialsProvider.class);
    }

    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        setAttribute(CREDS_PROVIDER, credentialsProvider);
    }

    public AuthCache getAuthCache() {
        return getAttribute(AUTH_CACHE, AuthCache.class);
    }

    public void setAuthCache(final AuthCache authCache) {
        setAttribute(AUTH_CACHE, authCache);
    }

    public AuthState getTargetAuthState() {
        return getAttribute(TARGET_AUTH_STATE, AuthState.class);
    }

    public AuthState getProxyAuthState() {
        return getAttribute(PROXY_AUTH_STATE, AuthState.class);
    }

    public <T> T getUserToken(final Class<T> clazz) {
        return getAttribute(USER_TOKEN, clazz);
    }

    public Object getUserToken() {
        return getAttribute(USER_TOKEN);
    }

    public void setUserToken(final Object obj) {
        setAttribute(USER_TOKEN, obj);
    }

    public RequestConfig getRequestConfig() {
        final RequestConfig config = getAttribute(REQUEST_CONFIG, RequestConfig.class);
        return config != null ? config : RequestConfig.DEFAULT;
    }

    public void setRequestConfig(final RequestConfig config) {
        setAttribute(REQUEST_CONFIG, config);
    }

}
