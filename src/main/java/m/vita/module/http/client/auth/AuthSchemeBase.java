package m.vita.module.http.client.auth;

import java.util.Locale;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.exception.AuthenticationException;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.header.FormattedHeader;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.HTTP;

@NotThreadSafe
public abstract class AuthSchemeBase implements ContextAwareAuthScheme {

    private ChallengeState challengeState;

    /**
     * Creates an instance of <tt>AuthSchemeBase</tt> with the given challenge
     * state.
     *
     * @since 4.2
     *
     * @deprecated (4.3) do not use.
     */
    @Deprecated
    public AuthSchemeBase(final ChallengeState challengeState) {
        super();
        this.challengeState = challengeState;
    }

    public AuthSchemeBase() {
        super();
    }

    /**
     * Processes the given challenge token. Some authentication schemes
     * may involve multiple challenge-response exchanges. Such schemes must be able
     * to maintain the state information when dealing with sequential challenges
     *
     * @param header the challenge header
     *
     * @throws MalformedChallengeException is thrown if the authentication challenge
     * is malformed
     */
    public void processChallenge(final Header header) throws MalformedChallengeException {
        Args.notNull(header, "Header");
        final String authheader = header.getName();
        if (authheader.equalsIgnoreCase(AUTH.WWW_AUTH)) {
            this.challengeState = ChallengeState.TARGET;
        } else if (authheader.equalsIgnoreCase(AUTH.PROXY_AUTH)) {
            this.challengeState = ChallengeState.PROXY;
        } else {
            throw new MalformedChallengeException("Unexpected header name: " + authheader);
        }

        final CharArrayBuffer buffer;
        int pos;
        if (header instanceof FormattedHeader) {
            buffer = ((FormattedHeader) header).getBuffer();
            pos = ((FormattedHeader) header).getValuePos();
        } else {
            final String s = header.getValue();
            if (s == null) {
                throw new MalformedChallengeException("Header value is null");
            }
            buffer = new CharArrayBuffer(s.length());
            buffer.append(s);
            pos = 0;
        }
        while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
            pos++;
        }
        final int beginIndex = pos;
        while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
            pos++;
        }
        final int endIndex = pos;
        final String s = buffer.substring(beginIndex, endIndex);
        if (!s.equalsIgnoreCase(getSchemeName())) {
            throw new MalformedChallengeException("Invalid scheme identifier: " + s);
        }

        parseChallenge(buffer, pos, buffer.length());
    }


    @SuppressWarnings("deprecation")
    public Header authenticate(
            final Credentials credentials,
            final HttpRequest request,
            final HttpContext context) throws AuthenticationException {
        return authenticate(credentials, request);
    }

    protected abstract void parseChallenge(
            CharArrayBuffer buffer, int beginIndex, int endIndex) throws MalformedChallengeException;

    /**
     * Returns <code>true</code> if authenticating against a proxy, <code>false</code>
     * otherwise.
     */
    public boolean isProxy() {
        return this.challengeState != null && this.challengeState == ChallengeState.PROXY;
    }

    /**
     * Returns {@link ChallengeState} value or <code>null</code> if unchallenged.
     *
     * @since 4.2
     */
    public ChallengeState getChallengeState() {
        return this.challengeState;
    }

    @Override
    public String toString() {
        final String name = getSchemeName();
        if (name != null) {
            return name.toUpperCase(Locale.ENGLISH);
        } else {
            return super.toString();
        }
    }

}
