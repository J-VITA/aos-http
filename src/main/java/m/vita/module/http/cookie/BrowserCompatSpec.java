package m.vita.module.http.cookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.header.BasicHeaderElement;
import m.vita.module.http.header.BasicHeaderValueFormatter;
import m.vita.module.http.header.FormattedHeader;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.header.NameValuePair;
import m.vita.module.http.header.ParserCursor;
import m.vita.module.http.message.BufferedHeader;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.DateUtils;
import m.vita.module.http.util.TextUtils;

@NotThreadSafe // superclass is @NotThreadSafe
public class BrowserCompatSpec extends CookieSpecBase {


    private static final String[] DEFAULT_DATE_PATTERNS = new String[] {
            DateUtils.PATTERN_RFC1123,
            DateUtils.PATTERN_RFC1036,
            DateUtils.PATTERN_ASCTIME,
            "EEE, dd-MMM-yyyy HH:mm:ss z",
            "EEE, dd-MMM-yyyy HH-mm-ss z",
            "EEE, dd MMM yy HH:mm:ss z",
            "EEE dd-MMM-yyyy HH:mm:ss z",
            "EEE dd MMM yyyy HH:mm:ss z",
            "EEE dd-MMM-yyyy HH-mm-ss z",
            "EEE dd-MMM-yy HH:mm:ss z",
            "EEE dd MMM yy HH:mm:ss z",
            "EEE,dd-MMM-yy HH:mm:ss z",
            "EEE,dd-MMM-yyyy HH:mm:ss z",
            "EEE, dd-MM-yyyy HH:mm:ss z",
    };

    private final String[] datepatterns;

    /** Default constructor */
    public BrowserCompatSpec(final String[] datepatterns, final BrowserCompatSpecFactory.SecurityLevel securityLevel) {
        super();
        if (datepatterns != null) {
            this.datepatterns = datepatterns.clone();
        } else {
            this.datepatterns = DEFAULT_DATE_PATTERNS;
        }
        switch (securityLevel) {
            case SECURITYLEVEL_DEFAULT:
                registerAttribHandler(ClientCookie.PATH_ATTR, new BasicPathHandler());
                break;
            case SECURITYLEVEL_IE_MEDIUM:
                registerAttribHandler(ClientCookie.PATH_ATTR, new BasicPathHandler() {
                            @Override
                            public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
                                // No validation
                            }
                        }
                );
                break;
            default:
                throw new RuntimeException("Unknown security level");
        }

        registerAttribHandler(ClientCookie.DOMAIN_ATTR, new BasicDomainHandler());
        registerAttribHandler(ClientCookie.MAX_AGE_ATTR, new BasicMaxAgeHandler());
        registerAttribHandler(ClientCookie.SECURE_ATTR, new BasicSecureHandler());
        registerAttribHandler(ClientCookie.COMMENT_ATTR, new BasicCommentHandler());
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, new BasicExpiresHandler(
                this.datepatterns));
        registerAttribHandler(ClientCookie.VERSION_ATTR, new BrowserCompatVersionAttributeHandler());
    }

    /** Default constructor */
    public BrowserCompatSpec(final String[] datepatterns) {
        this(datepatterns, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    /** Default constructor */
    public BrowserCompatSpec() {
        this(null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    public List<Cookie> parse(final Header header, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        final String headername = header.getName();
        if (!headername.equalsIgnoreCase(StateManagement.SET_COOKIE)) {
            throw new MalformedCookieException("Unrecognized cookie header '"
                    + header.toString() + "'");
        }
        final HeaderElement[] helems = header.getElements();
        boolean versioned = false;
        boolean netscape = false;
        for (final HeaderElement helem: helems) {
            if (helem.getParameterByName("version") != null) {
                versioned = true;
            }
            if (helem.getParameterByName("expires") != null) {
                netscape = true;
            }
        }
        if (netscape || !versioned) {
            // Need to parse the header again, because Netscape style cookies do not correctly
            // support multiple header elements (comma cannot be treated as an element separator)
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
            final HeaderElement elem = parser.parseHeader(buffer, cursor);
            final String name = elem.getName();
            final String value = elem.getValue();
            if (name == null || TextUtils.isBlank(name)) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            final BasicClientCookie cookie = new BasicClientCookie(name, value);
            cookie.setPath(getDefaultPath(origin));
            cookie.setDomain(getDefaultDomain(origin));

            // cycle through the parameters
            final NameValuePair[] attribs = elem.getParameters();
            for (int j = attribs.length - 1; j >= 0; j--) {
                final NameValuePair attrib = attribs[j];
                final String s = attrib.getName().toLowerCase(Locale.ENGLISH);
                cookie.setAttribute(s, attrib.getValue());
                final CookieAttributeHandler handler = findAttribHandler(s);
                if (handler != null) {
                    handler.parse(cookie, attrib.getValue());
                }
            }
            // Override version for Netscape style cookies
            if (netscape) {
                cookie.setVersion(0);
            }
            return Collections.<Cookie>singletonList(cookie);
        } else {
            return parse(helems, origin);
        }
    }

    private static boolean isQuoteEnclosed(final String s) {
        return s != null && s.startsWith("\"") && s.endsWith("\"");
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
            final String cookieName = cookie.getName();
            final String cookieValue = cookie.getValue();
            if (cookie.getVersion() > 0 && !isQuoteEnclosed(cookieValue)) {
                BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(
                        buffer,
                        new BasicHeaderElement(cookieName, cookieValue),
                        false);
            } else {
                // Netscape style cookies do not support quoted values
                buffer.append(cookieName);
                buffer.append("=");
                if (cookieValue != null) {
                    buffer.append(cookieValue);
                }
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
        return "compatibility";
    }

}
