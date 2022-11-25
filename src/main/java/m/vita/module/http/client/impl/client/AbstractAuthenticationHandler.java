package m.vita.module.http.client.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.AuthenticationHandler;
import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.client.auth.AuthSchemeRegistry;
import m.vita.module.http.exception.AuthenticationException;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.header.FormattedHeader;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.params.AuthPolicy;
import m.vita.module.http.client.protocol.ClientContext;
import m.vita.module.http.util.Asserts;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.HTTP;
import m.vita.module.http.util.HttpClientAndroidLog;

@Immutable
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {

    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    private static final List<String> DEFAULT_SCHEME_PRIORITY =
            Collections.unmodifiableList(Arrays.asList(new String[] {
                    AuthPolicy.SPNEGO,
                    AuthPolicy.NTLM,
                    AuthPolicy.DIGEST,
                    AuthPolicy.BASIC
            }));

    public AbstractAuthenticationHandler() {
        super();
    }

    protected Map<String, Header> parseChallenges(
            final Header[] headers) throws MalformedChallengeException {

        final Map<String, Header> map = new HashMap<String, Header>(headers.length);
        for (final Header header : headers) {
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
            map.put(s.toLowerCase(Locale.ENGLISH), header);
        }
        return map;
    }

    /**
     * Returns default list of auth scheme names in their order of preference.
     *
     * @return list of auth scheme names
     */
    protected List<String> getAuthPreferences() {
        return DEFAULT_SCHEME_PRIORITY;
    }

    /**
     * Returns default list of auth scheme names in their order of preference
     * based on the HTTP response and the current execution context.
     *
     * @param response HTTP response.
     * @param context HTTP execution context.
     *
     * @since 4.1
     */
    protected List<String> getAuthPreferences(
            final HttpResponse response,
            final HttpContext context) {
        return getAuthPreferences();
    }

    public AuthScheme selectScheme(
            final Map<String, Header> challenges,
            final HttpResponse response,
            final HttpContext context) throws AuthenticationException {

        final AuthSchemeRegistry registry = (AuthSchemeRegistry) context.getAttribute(
                ClientContext.AUTHSCHEME_REGISTRY);
        Asserts.notNull(registry, "AuthScheme registry");
        Collection<String> authPrefs = getAuthPreferences(response, context);
        if (authPrefs == null) {
            authPrefs = DEFAULT_SCHEME_PRIORITY;
        }

        if (this.log.isDebugEnabled()) {
            this.log.debug("Authentication schemes in the order of preference: "
                    + authPrefs);
        }

        AuthScheme authScheme = null;
        for (final String id: authPrefs) {
            final Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));

            if (challenge != null) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(id + " authentication scheme selected");
                }
                try {
                    authScheme = registry.getAuthScheme(id, response.getParams());
                    break;
                } catch (final IllegalStateException e) {
                    if (this.log.isWarnEnabled()) {
                        this.log.warn("Authentication scheme " + id + " not supported");
                        // Try again
                    }
                }
            } else {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Challenge for " + id + " authentication scheme not available");
                    // Try again
                }
            }
        }
        if (authScheme == null) {
            // If none selected, something is wrong
            throw new AuthenticationException(
                    "Unable to respond to any of these challenges: "
                            + challenges);
        }
        return authScheme;
    }

}
