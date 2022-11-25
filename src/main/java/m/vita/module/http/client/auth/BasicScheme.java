package m.vita.module.http.client.auth;

import java.nio.charset.Charset;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.exception.AuthenticationException;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.execute.BasicHttpContext;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.message.BufferedHeader;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.Base64;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.Consts;
import m.vita.module.http.util.EncodingUtils;
import m.vita.module.http.exception.InvalidCredentialsException;

@NotThreadSafe
public class BasicScheme extends RFC2617Scheme {

    /* Base64 instance removed by HttpClient for Android script. */
    /** Whether the basic authentication process is complete */
    private boolean complete;

    /**
     * @since 4.3
     */
    public BasicScheme(final Charset credentialsCharset) {
        super(credentialsCharset);
        /* Base64 instance removed by HttpClient for Android script. */
        this.complete = false;
    }

    /**
     * Creates an instance of <tt>BasicScheme</tt> with the given challenge
     * state.
     *
     * @since 4.2
     *
     * @deprecated (4.3) do not use.
     */
    @Deprecated
    public BasicScheme(final ChallengeState challengeState) {
        super(challengeState);
        /* Base64 instance removed by HttpClient for Android script. */
    }

    public BasicScheme() {
        this(Consts.ASCII);
    }

    /**
     * Returns textual designation of the basic authentication scheme.
     *
     * @return <code>basic</code>
     */
    public String getSchemeName() {
        return "basic";
    }

    /**
     * Processes the Basic challenge.
     *
     * @param header the challenge header
     *
     * @throws MalformedChallengeException is thrown if the authentication challenge
     * is malformed
     */
    @Override
    public void processChallenge(
            final Header header) throws MalformedChallengeException {
        super.processChallenge(header);
        this.complete = true;
    }

    /**
     * Tests if the Basic authentication process has been completed.
     *
     * @return <tt>true</tt> if Basic authorization has been processed,
     *   <tt>false</tt> otherwise.
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Returns <tt>false</tt>. Basic authentication scheme is request based.
     *
     * @return <tt>false</tt>.
     */
    public boolean isConnectionBased() {
        return false;
    }

    /**
     * @deprecated (4.2) Use {@link ContextAwareAuthScheme#authenticate(
     *   Credentials, HttpRequest, HttpContext)}
     */
    @Deprecated
    public Header authenticate(
            final Credentials credentials, final HttpRequest request) throws AuthenticationException {
        return authenticate(credentials, request, new BasicHttpContext());
    }

    /**
     * Produces basic authorization header for the given set of {@link Credentials}.
     *
     * @param credentials The set of credentials to be used for authentication
     * @param request The request being authenticated
     * @throws InvalidCredentialsException if authentication
     *   credentials are not valid or not applicable for this authentication scheme
     * @throws AuthenticationException if authorization string cannot
     *   be generated due to an authentication failure
     *
     * @return a basic authorization string
     */
    @Override
    public Header authenticate(
            final Credentials credentials,
            final HttpRequest request,
            final HttpContext context) throws AuthenticationException {

        Args.notNull(credentials, "Credentials");
        Args.notNull(request, "HTTP request");
        final StringBuilder tmp = new StringBuilder();
        tmp.append(credentials.getUserPrincipal().getName());
        tmp.append(":");
        tmp.append((credentials.getPassword() == null) ? "null" : credentials.getPassword());

        final byte[] base64password = Base64.encode(
                EncodingUtils.getBytes(tmp.toString(), getCredentialsCharset(request)), Base64.NO_WRAP);

        final CharArrayBuffer buffer = new CharArrayBuffer(32);
        if (isProxy()) {
            buffer.append(AUTH.PROXY_AUTH_RESP);
        } else {
            buffer.append(AUTH.WWW_AUTH_RESP);
        }
        buffer.append(": Basic ");
        buffer.append(base64password, 0, base64password.length);

        return new BufferedHeader(buffer);
    }

    /**
     * Returns a basic <tt>Authorization</tt> header value for the given
     * {@link Credentials} and charset.
     *
     * @param credentials The credentials to encode.
     * @param charset The charset to use for encoding the credentials
     *
     * @return a basic authorization header
     *
     * @deprecated (4.3) use {@link #authenticate(Credentials, HttpRequest, HttpContext)}.
     */
    @Deprecated
    public static Header authenticate(
            final Credentials credentials,
            final String charset,
            final boolean proxy) {
        Args.notNull(credentials, "Credentials");
        Args.notNull(charset, "charset");

        final StringBuilder tmp = new StringBuilder();
        tmp.append(credentials.getUserPrincipal().getName());
        tmp.append(":");
        tmp.append((credentials.getPassword() == null) ? "null" : credentials.getPassword());

        final byte[] base64password = Base64.encode(
                EncodingUtils.getBytes(tmp.toString(), charset), Base64.NO_WRAP);

        final CharArrayBuffer buffer = new CharArrayBuffer(32);
        if (proxy) {
            buffer.append(AUTH.PROXY_AUTH_RESP);
        } else {
            buffer.append(AUTH.WWW_AUTH_RESP);
        }
        buffer.append(": Basic ");
        buffer.append(base64password, 0, base64password.length);

        return new BufferedHeader(buffer);
    }

}
