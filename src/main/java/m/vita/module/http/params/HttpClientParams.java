package m.vita.module.http.params;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.connect.HttpConnectionParams;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.util.Args;

@Immutable
public class HttpClientParams {

    private HttpClientParams() {
        super();
    }

    public static boolean isRedirecting(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter
                (ClientPNames.HANDLE_REDIRECTS, true);
    }

    public static void setRedirecting(final HttpParams params, final boolean value) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter
                (ClientPNames.HANDLE_REDIRECTS, value);
    }

    public static boolean isAuthenticating(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter
                (ClientPNames.HANDLE_AUTHENTICATION, true);
    }

    public static void setAuthenticating(final HttpParams params, final boolean value) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter
                (ClientPNames.HANDLE_AUTHENTICATION, value);
    }

    public static String getCookiePolicy(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        final String cookiePolicy = (String)
                params.getParameter(ClientPNames.COOKIE_POLICY);
        if (cookiePolicy == null) {
            return CookiePolicy.BEST_MATCH;
        }
        return cookiePolicy;
    }

    public static void setCookiePolicy(final HttpParams params, final String cookiePolicy) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter(ClientPNames.COOKIE_POLICY, cookiePolicy);
    }

    /**
     * Set the parameter {@code ClientPNames.CONN_MANAGER_TIMEOUT}.
     *
     * @since 4.2
     */
    public static void setConnectionManagerTimeout(final HttpParams params, final long timeout) {
        Args.notNull(params, "HTTP parameters");
        params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, timeout);
    }

    /**
     * Get the connectiion manager timeout value.
     * This is defined by the parameter {@code ClientPNames.CONN_MANAGER_TIMEOUT}.
     * Failing that it uses the parameter {@code CoreConnectionPNames.CONNECTION_TIMEOUT}
     * which defaults to 0 if not defined.
     *
     * @since 4.2
     * @return the timeout value
     */
    public static long getConnectionManagerTimeout(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        final Long timeout = (Long) params.getParameter(ClientPNames.CONN_MANAGER_TIMEOUT);
        if (timeout != null) {
            return timeout.longValue();
        }
        return HttpConnectionParams.getConnectionTimeout(params);
    }

}
