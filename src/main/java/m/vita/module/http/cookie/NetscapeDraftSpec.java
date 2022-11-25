package m.vita.module.http.cookie;

import java.util.ArrayList;
import java.util.List;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.header.FormattedHeader;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.header.ParserCursor;
import m.vita.module.http.message.BufferedHeader;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.CharArrayBuffer;

@NotThreadSafe // superclass is @NotThreadSafe
public class NetscapeDraftSpec extends CookieSpecBase {

    protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";

    private final String[] datepatterns;

    /** Default constructor */
    public NetscapeDraftSpec(final String[] datepatterns) {
        super();
        if (datepatterns != null) {
            this.datepatterns = datepatterns.clone();
        } else {
            this.datepatterns = new String[] { EXPIRES_PATTERN };
        }
        registerAttribHandler(ClientCookie.PATH_ATTR, new BasicPathHandler());
        registerAttribHandler(ClientCookie.DOMAIN_ATTR, new NetscapeDomainHandler());
        registerAttribHandler(ClientCookie.SECURE_ATTR, new BasicSecureHandler());
        registerAttribHandler(ClientCookie.COMMENT_ATTR, new BasicCommentHandler());
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, new BasicExpiresHandler(
                this.datepatterns));
    }

    /** Default constructor */
    public NetscapeDraftSpec() {
        this(null);
    }

    /**
     * Parses the Set-Cookie value into an array of <tt>Cookie</tt>s.
     *
     * <p>Syntax of the Set-Cookie HTTP Response Header:</p>
     *
     * <p>This is the format a CGI script would use to add to
     * the HTTP headers a new piece of data which is to be stored by
     * the client for later retrieval.</p>
     *
     * <PRE>
     *  Set-Cookie: NAME=VALUE; expires=DATE; path=PATH; domain=DOMAIN_NAME; secure
     * </PRE>
     *
     * <p>Please note that the Netscape draft specification does not fully conform to the HTTP
     * header format. Comma character if present in <code>Set-Cookie</code> will not be treated
     * as a header element separator</p>
     *
     * @see <a href="http://web.archive.org/web/20020803110822/http://wp.netscape.com/newsref/std/cookie_spec.html">
     *  The Cookie Spec.</a>
     *
     * @param header the <tt>Set-Cookie</tt> received from the server
     * @return an array of <tt>Cookie</tt>s parsed from the Set-Cookie value
     * @throws MalformedCookieException if an exception occurs during parsing
     */
    public List<Cookie> parse(final Header header, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        if (!header.getName().equalsIgnoreCase(StateManagement.SET_COOKIE)) {
            throw new MalformedCookieException("Unrecognized cookie header '"
                    + header.toString() + "'");
        }
        final NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
        final CharArrayBuffer buffer;
        final ParserCursor cursor;
        if (header instanceof FormattedHeader) {
            buffer = ((FormattedHeader) header).getBuffer();
            cursor = new ParserCursor(
                    ((FormattedHeader) header).getValuePos(),
                    buffer.length());
        } else {
            final String s = header.getValue();
            if (s == null) {
                throw new MalformedCookieException("Header value is null");
            }
            buffer = new CharArrayBuffer(s.length());
            buffer.append(s);
            cursor = new ParserCursor(0, buffer.length());
        }
        return parse(new HeaderElement[] { parser.parseHeader(buffer, cursor) }, origin);
    }

    public List<Header> formatCookies(final List<Cookie> cookies) {
        Args.notEmpty(cookies, "List of cookies");
        final CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
        buffer.append(StateManagement.COOKIE);
        buffer.append(": ");
        for (int i = 0; i < cookies.size(); i++) {
            final Cookie cookie = cookies.get(i);
            if (i > 0) {
                buffer.append("; ");
            }
            buffer.append(cookie.getName());
            final String s = cookie.getValue();
            if (s != null) {
                buffer.append("=");
                buffer.append(s);
            }
        }
        final List<Header> headers = new ArrayList<Header>(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }

    public int getVersion() {
        return 0;
    }

    public Header getVersionHeader() {
        return null;
    }

    @Override
    public String toString() {
        return "netscape";
    }

}
