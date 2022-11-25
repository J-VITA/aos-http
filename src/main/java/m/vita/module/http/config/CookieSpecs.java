package m.vita.module.http.config;

import m.vita.module.http.annotation.Immutable;

@Immutable
public final class CookieSpecs {

    /**
     * The policy that provides high degree of compatibility
     * with common cookie management of popular HTTP agents.
     */
    public static final String BROWSER_COMPATIBILITY = "compatibility";

    /**
     * The Netscape cookie draft compliant policy.
     */
    public static final String NETSCAPE = "netscape";

    /**
     * The RFC 2965 compliant policy (standard).
     */
    public static final String STANDARD = "standard";

    /**
     * The default 'best match' policy.
     */
    public static final String BEST_MATCH = "best-match";

    /**
     * The policy that ignores cookies.
     */
    public static final String IGNORE_COOKIES = "ignoreCookies";

    private CookieSpecs() {
    }

}
