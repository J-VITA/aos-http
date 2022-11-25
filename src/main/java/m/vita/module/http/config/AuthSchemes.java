package m.vita.module.http.config;

import m.vita.module.http.annotation.Immutable;

@Immutable
public final class AuthSchemes {

    /**
     * Basic authentication scheme as defined in RFC2617 (considered inherently
     * insecure, but most widely supported)
     */
    public static final String BASIC = "Basic";

    /**
     * Digest authentication scheme as defined in RFC2617.
     */
    public static final String DIGEST = "Digest";

    /**
     * The NTLM scheme is a proprietary Microsoft Windows Authentication
     * protocol (considered to be the most secure among currently supported
     * authentication schemes).
     */
    public static final String NTLM = "NTLM";

    /**
     * SPNEGO Authentication scheme.
     */
    public static final String SPNEGO = "negotiate";

    /**
     * Kerberos Authentication scheme.
     */
    public static final String KERBEROS = "Kerberos";

    private AuthSchemes() {
    }

}
