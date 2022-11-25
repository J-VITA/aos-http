package m.vita.module.http.cookie;

import java.util.StringTokenizer;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.util.Args;

@Immutable
public class RFC2965PortAttributeHandler implements CookieAttributeHandler {

    public RFC2965PortAttributeHandler() {
        super();
    }

    /**
     * Parses the given Port attribute value (e.g. "8000,8001,8002")
     * into an array of ports.
     *
     * @param portValue port attribute value
     * @return parsed array of ports
     * @throws MalformedCookieException if there is a problem in
     *          parsing due to invalid portValue.
     */
    private static int[] parsePortAttribute(final String portValue)
            throws MalformedCookieException {
        final StringTokenizer st = new StringTokenizer(portValue, ",");
        final int[] ports = new int[st.countTokens()];
        try {
            int i = 0;
            while(st.hasMoreTokens()) {
                ports[i] = Integer.parseInt(st.nextToken().trim());
                if (ports[i] < 0) {
                    throw new MalformedCookieException ("Invalid Port attribute.");
                }
                ++i;
            }
        } catch (final NumberFormatException e) {
            throw new MalformedCookieException ("Invalid Port "
                    + "attribute: " + e.getMessage());
        }
        return ports;
    }

    /**
     * Returns <tt>true</tt> if the given port exists in the given
     * ports list.
     *
     * @param port port of host where cookie was received from or being sent to.
     * @param ports port list
     * @return true returns <tt>true</tt> if the given port exists in
     *         the given ports list; <tt>false</tt> otherwise.
     */
    private static boolean portMatch(final int port, final int[] ports) {
        boolean portInList = false;
        for (final int port2 : ports) {
            if (port == port2) {
                portInList = true;
                break;
            }
        }
        return portInList;
    }

    /**
     * Parse cookie port attribute.
     */
    public void parse(final SetCookie cookie, final String portValue)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (cookie instanceof SetCookie2) {
            final SetCookie2 cookie2 = (SetCookie2) cookie;
            if (portValue != null && portValue.trim().length() > 0) {
                final int[] ports = parsePortAttribute(portValue);
                cookie2.setPorts(ports);
            }
        }
    }

    /**
     * Validate cookie port attribute. If the Port attribute was specified
     * in header, the request port must be in cookie's port list.
     */
    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final int port = origin.getPort();
        if (cookie instanceof ClientCookie
                && ((ClientCookie) cookie).containsAttribute(ClientCookie.PORT_ATTR)) {
            if (!portMatch(port, cookie.getPorts())) {
                throw new CookieRestrictionViolationException(
                        "Port attribute violates RFC 2965: "
                                + "Request port not found in cookie's port list.");
            }
        }
    }

    /**
     * Match cookie port attribute. If the Port attribute is not specified
     * in header, the cookie can be sent to any port. Otherwise, the request port
     * must be in the cookie's port list.
     */
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final int port = origin.getPort();
        if (cookie instanceof ClientCookie
                && ((ClientCookie) cookie).containsAttribute(ClientCookie.PORT_ATTR)) {
            if (cookie.getPorts() == null) {
                // Invalid cookie state: port not specified
                return false;
            }
            if (!portMatch(port, cookie.getPorts())) {
                return false;
            }
        }
        return true;
    }

}
