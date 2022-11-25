package m.vita.module.http.cookie;

import java.util.List;

import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.header.Header;

public interface CookieSpec {

    /**
     * Returns version of the state management this cookie specification
     * conforms to.
     *
     * @return version of the state management specification
     */
    int getVersion();

    /**
     * Parse the <tt>"Set-Cookie"</tt> Header into an array of Cookies.
     *
     * <p>This method will not perform the validation of the resultant
     * {@link Cookie}s</p>
     *
     * @see #validate
     *
     * @param header the <tt>Set-Cookie</tt> received from the server
     * @param origin details of the cookie origin
     * @return an array of <tt>Cookie</tt>s parsed from the header
     * @throws MalformedCookieException if an exception occurs during parsing
     */
    List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException;

    /**
     * Validate the cookie according to validation rules defined by the
     *  cookie specification.
     *
     * @param cookie the Cookie to validate
     * @param origin details of the cookie origin
     * @throws MalformedCookieException if the cookie is invalid
     */
    void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException;

    /**
     * Determines if a Cookie matches the target location.
     *
     * @param cookie the Cookie to be matched
     * @param origin the target to test against
     *
     * @return <tt>true</tt> if the cookie should be submitted with a request
     *  with given attributes, <tt>false</tt> otherwise.
     */
    boolean match(Cookie cookie, CookieOrigin origin);

    /**
     * Create <tt>"Cookie"</tt> headers for an array of Cookies.
     *
     * @param cookies the Cookies format into a Cookie header
     * @return a Header for the given Cookies.
     * @throws IllegalArgumentException if an input parameter is illegal
     */
    List<Header> formatCookies(List<Cookie> cookies);

    /**
     * Returns a request header identifying what version of the state management
     * specification is understood. May be <code>null</code> if the cookie
     * specification does not support <tt>Cookie2</tt> header.
     */
    Header getVersionHeader();

}
