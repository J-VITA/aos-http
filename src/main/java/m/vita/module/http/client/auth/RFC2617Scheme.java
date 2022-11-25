package m.vita.module.http.client.auth;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.client.auth.params.AuthPNames;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.header.BasicHeaderValueParser;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.header.HeaderValueParser;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.ParserCursor;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.Consts;

@NotThreadSafe // AuthSchemeBase, params
public abstract class RFC2617Scheme extends AuthSchemeBase {

    private final Map<String, String> params;
    private final Charset credentialsCharset;

    /**
     * Creates an instance of <tt>RFC2617Scheme</tt> with the given challenge
     * state.
     *
     * @since 4.2
     *
     * @deprecated (4.3) do not use.
     */
    @Deprecated
    public RFC2617Scheme(final ChallengeState challengeState) {
        super(challengeState);
        this.params = new HashMap<String, String>();
        this.credentialsCharset = Consts.ASCII;
    }

    /**
     * @since 4.3
     */
    public RFC2617Scheme(final Charset credentialsCharset) {
        super();
        this.params = new HashMap<String, String>();
        this.credentialsCharset = credentialsCharset != null ? credentialsCharset : Consts.ASCII;
    }

    public RFC2617Scheme() {
        this(Consts.ASCII);
    }


    /**
     * @since 4.3
     */
    public Charset getCredentialsCharset() {
        return credentialsCharset;
    }

    String getCredentialsCharset(final HttpRequest request) {
        String charset = (String) request.getParams().getParameter(AuthPNames.CREDENTIAL_CHARSET);
        if (charset == null) {
            charset = getCredentialsCharset().name();
        }
        return charset;
    }

    @Override
    protected void parseChallenge(
            final CharArrayBuffer buffer, final int pos, final int len) throws MalformedChallengeException {
        final HeaderValueParser parser = BasicHeaderValueParser.INSTANCE;
        final ParserCursor cursor = new ParserCursor(pos, buffer.length());
        final HeaderElement[] elements = parser.parseElements(buffer, cursor);
        if (elements.length == 0) {
            throw new MalformedChallengeException("Authentication challenge is empty");
        }
        this.params.clear();
        for (final HeaderElement element : elements) {
            this.params.put(element.getName().toLowerCase(Locale.ENGLISH), element.getValue());
        }
    }

    /**
     * Returns authentication parameters map. Keys in the map are lower-cased.
     *
     * @return the map of authentication parameters
     */
    protected Map<String, String> getParameters() {
        return this.params;
    }

    /**
     * Returns authentication parameter with the given name, if available.
     *
     * @param name The name of the parameter to be returned
     *
     * @return the parameter with the given name
     */
    public String getParameter(final String name) {
        if (name == null) {
            return null;
        }
        return this.params.get(name.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Returns authentication realm. The realm may not be null.
     *
     * @return the authentication realm
     */
    public String getRealm() {
        return getParameter("realm");
    }

}
