package m.vita.module.http.cookie;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.message.BufferedHeader;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.DateUtils;

@NotThreadSafe // superclass is @NotThreadSafe
public class RFC2109Spec extends CookieSpecBase {

    private final static CookiePathComparator PATH_COMPARATOR = new CookiePathComparator();

    private final static String[] DATE_PATTERNS = {
            DateUtils.PATTERN_RFC1123,
            DateUtils.PATTERN_RFC1036,
            DateUtils.PATTERN_ASCTIME
    };

    private final String[] datepatterns;
    private final boolean oneHeader;

    /** Default constructor */
    public RFC2109Spec(final String[] datepatterns, final boolean oneHeader) {
        super();
        if (datepatterns != null) {
            this.datepatterns = datepatterns.clone();
        } else {
            this.datepatterns = DATE_PATTERNS;
        }
        this.oneHeader = oneHeader;
        registerAttribHandler(ClientCookie.VERSION_ATTR, new RFC2109VersionHandler());
        registerAttribHandler(ClientCookie.PATH_ATTR, new BasicPathHandler());
        registerAttribHandler(ClientCookie.DOMAIN_ATTR, new RFC2109DomainHandler());
        registerAttribHandler(ClientCookie.MAX_AGE_ATTR, new BasicMaxAgeHandler());
        registerAttribHandler(ClientCookie.SECURE_ATTR, new BasicSecureHandler());
        registerAttribHandler(ClientCookie.COMMENT_ATTR, new BasicCommentHandler());
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, new BasicExpiresHandler(
                this.datepatterns));
    }

    /** Default constructor */
    public RFC2109Spec() {
        this(null, false);
    }

    public List<Cookie> parse(final Header header, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        if (!header.getName().equalsIgnoreCase(StateManagement.SET_COOKIE)) {
            throw new MalformedCookieException("Unrecognized cookie header '"
                    + header.toString() + "'");
        }
        final HeaderElement[] elems = header.getElements();
        return parse(elems, origin);
    }

    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        final String name = cookie.getName();
        if (name.indexOf(' ') != -1) {
            throw new CookieRestrictionViolationException("Cookie name may not contain blanks");
        }
        if (name.startsWith("$")) {
            throw new CookieRestrictionViolationException("Cookie name may not start with $");
        }
        super.validate(cookie, origin);
    }

    public List<Header> formatCookies(final List<Cookie> cookies) {
        Args.notEmpty(cookies, "List of cookies");
        List<Cookie> cookieList;
        if (cookies.size() > 1) {
            // Create a mutable copy and sort the copy.
            cookieList = new ArrayList<Cookie>(cookies);
            Collections.sort(cookieList, PATH_COMPARATOR);
        } else {
            cookieList = cookies;
        }
        if (this.oneHeader) {
            return doFormatOneHeader(cookieList);
        } else {
            return doFormatManyHeaders(cookieList);
        }
    }

    private List<Header> doFormatOneHeader(final List<Cookie> cookies) {
        int version = Integer.MAX_VALUE;
        // Pick the lowest common denominator
        for (final Cookie cookie : cookies) {
            if (cookie.getVersion() < version) {
                version = cookie.getVersion();
            }
        }
        final CharArrayBuffer buffer = new CharArrayBuffer(40 * cookies.size());
        buffer.append(StateManagement.COOKIE);
        buffer.append(": ");
        buffer.append("$Version=");
        buffer.append(Integer.toString(version));
        for (final Cookie cooky : cookies) {
            buffer.append("; ");
            final Cookie cookie = cooky;
            formatCookieAsVer(buffer, cookie, version);
        }
        final List<Header> headers = new ArrayList<Header>(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }

    private List<Header> doFormatManyHeaders(final List<Cookie> cookies) {
        final List<Header> headers = new ArrayList<Header>(cookies.size());
        for (final Cookie cookie : cookies) {
            final int version = cookie.getVersion();
            final CharArrayBuffer buffer = new CharArrayBuffer(40);
            buffer.append("Cookie: ");
            buffer.append("$Version=");
            buffer.append(Integer.toString(version));
            buffer.append("; ");
            formatCookieAsVer(buffer, cookie, version);
            headers.add(new BufferedHeader(buffer));
        }
        return headers;
    }

    /**
     * Return a name/value string suitable for sending in a <tt>"Cookie"</tt>
     * header as defined in RFC 2109 for backward compatibility with cookie
     * version 0
     * @param buffer The char array buffer to use for output
     * @param name The cookie name
     * @param value The cookie value
     * @param version The cookie version
     */
    protected void formatParamAsVer(final CharArrayBuffer buffer,
                                    final String name, final String value, final int version) {
        buffer.append(name);
        buffer.append("=");
        if (value != null) {
            if (version > 0) {
                buffer.append('\"');
                buffer.append(value);
                buffer.append('\"');
            } else {
                buffer.append(value);
            }
        }
    }

    /**
     * Return a string suitable for sending in a <tt>"Cookie"</tt> header
     * as defined in RFC 2109 for backward compatibility with cookie version 0
     * @param buffer The char array buffer to use for output
     * @param cookie The {@link Cookie} to be formatted as string
     * @param version The version to use.
     */
    protected void formatCookieAsVer(final CharArrayBuffer buffer,
                                     final Cookie cookie, final int version) {
        formatParamAsVer(buffer, cookie.getName(), cookie.getValue(), version);
        if (cookie.getPath() != null) {
            if (cookie instanceof ClientCookie
                    && ((ClientCookie) cookie).containsAttribute(ClientCookie.PATH_ATTR)) {
                buffer.append("; ");
                formatParamAsVer(buffer, "$Path", cookie.getPath(), version);
            }
        }
        if (cookie.getDomain() != null) {
            if (cookie instanceof ClientCookie
                    && ((ClientCookie) cookie).containsAttribute(ClientCookie.DOMAIN_ATTR)) {
                buffer.append("; ");
                formatParamAsVer(buffer, "$Domain", cookie.getDomain(), version);
            }
        }
    }

    public int getVersion() {
        return 1;
    }

    public Header getVersionHeader() {
        return null;
    }

    @Override
    public String toString() {
        return "rfc2109";
    }

}
