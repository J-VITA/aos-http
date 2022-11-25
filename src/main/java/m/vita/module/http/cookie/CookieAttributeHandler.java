package m.vita.module.http.cookie;

import m.vita.module.http.exception.MalformedCookieException;

public interface CookieAttributeHandler {

    /**
     * Parse the given cookie attribute value and update the corresponding
     * {@link Cookie} property.
     *
     * @param cookie {@link Cookie} to be updated
     * @param value cookie attribute value from the cookie response header
     */
    void parse(SetCookie cookie, String value)
            throws MalformedCookieException;

    /**
     * Peforms cookie validation for the given attribute value.
     *
     * @param cookie {@link Cookie} to validate
     * @param origin the cookie source to validate against
     * @throws MalformedCookieException if cookie validation fails for this attribute
     */
    void validate(Cookie cookie, CookieOrigin origin)
            throws MalformedCookieException;

    /**
     * Matches the given value (property of the destination host where request is being
     * submitted) with the corresponding cookie attribute.
     *
     * @param cookie {@link Cookie} to match
     * @param origin the cookie source to match against
     * @return <tt>true</tt> if the match is successful; <tt>false</tt> otherwise
     */
    boolean match(Cookie cookie, CookieOrigin origin);

}
