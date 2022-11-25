package m.vita.module.http.client.auth.params;

import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.client.auth.Credentials;

public interface AuthPNames {

    /**
     * Defines the charset to be used when encoding
     * {@link Credentials}.
     * <p>
     * This parameter expects a value of type {@link String}.
     */
    public static final String CREDENTIAL_CHARSET = "http.auth.credential-charset";

    /**
     * Defines the order of preference for supported
     *  {@link AuthScheme}s when authenticating with
     *  the target host.
     * <p>
     * This parameter expects a value of type {@link java.util.Collection}. The
     * collection is expected to contain {@link String} instances representing
     * a name of an authentication scheme as returned by
     * {@link AuthScheme#getSchemeName()}.
     */
    public static final String TARGET_AUTH_PREF = "http.auth.target-scheme-pref";

    /**
     * Defines the order of preference for supported
     *  {@link AuthScheme}s when authenticating with the
     *  proxy host.
     * <p>
     * This parameter expects a value of type {@link java.util.Collection}. The
     * collection is expected to contain {@link String} instances representing
     * a name of an authentication scheme as returned by
     * {@link AuthScheme#getSchemeName()}.
     */
    public static final String PROXY_AUTH_PREF = "http.auth.proxy-scheme-pref";

}
