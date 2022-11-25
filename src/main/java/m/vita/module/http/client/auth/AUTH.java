package m.vita.module.http.client.auth;

import m.vita.module.http.annotation.Immutable;

@Immutable
public final class AUTH {

    /**
     * The www authenticate challange header.
     */
    public static final String WWW_AUTH = "WWW-Authenticate";

    /**
     * The www authenticate response header.
     */
    public static final String WWW_AUTH_RESP = "Authorization";

    /**
     * The proxy authenticate challange header.
     */
    public static final String PROXY_AUTH = "Proxy-Authenticate";

    /**
     * The proxy authenticate response header.
     */
    public static final String PROXY_AUTH_RESP = "Proxy-Authorization";

    private AUTH() {
    }

}
