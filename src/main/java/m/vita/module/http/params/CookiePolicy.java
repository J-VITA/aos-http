package m.vita.module.http.params;

import m.vita.module.http.annotation.Immutable;

@Immutable
public final class CookiePolicy {

    /**
     * The policy that provides high degree of compatibilty
     * with common cookie management of popular HTTP agents.
     */
    public static final String BROWSER_COMPATIBILITY = "compatibility";

    /**
     * The Netscape cookie draft compliant policy.
     */
    public static final String NETSCAPE = "netscape";

    /**
     * The RFC 2109 compliant policy.
     */
    public static final String RFC_2109 = "rfc2109";

    /**
     * The RFC 2965 compliant policy.
     */
    public static final String RFC_2965 = "rfc2965";

    /**
     * The default 'best match' policy.
     */
    public static final String BEST_MATCH = "best-match";

    /**
     * The policy that ignores cookies.
     *
     * @since 4.1-beta1
     */
    public static final String IGNORE_COOKIES = "ignoreCookies";

    private CookiePolicy() {
        super();
    }

}
