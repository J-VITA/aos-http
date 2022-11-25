package m.vita.module.http.params;

import m.vita.module.http.annotation.Immutable;

@Immutable
public final class AuthPolicy {

    private AuthPolicy() {
        super();
    }

    /**
     * The NTLM scheme is a proprietary Microsoft Windows Authentication
     * protocol (considered to be the most secure among currently supported
     * authentication schemes).
     */
    public static final String NTLM = "NTLM";

    /**
     * Digest authentication scheme as defined in RFC2617.
     */
    public static final String DIGEST = "Digest";

    /**
     * Basic authentication scheme as defined in RFC2617 (considered inherently
     * insecure, but most widely supported)
     */
    public static final String BASIC = "Basic";

    /**
     * SPNEGO Authentication scheme.
     *
     * @since 4.1
     */
    public static final String SPNEGO = "negotiate";

    /**
     * Kerberos Authentication scheme.
     *
     * @since 4.2
     */
    public static final String KERBEROS = "Kerberos";

}
