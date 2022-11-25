package m.vita.module.http.cookie;

import java.util.Locale;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.util.Args;

@Immutable
public class RFC2109DomainHandler implements CookieAttributeHandler {

    public RFC2109DomainHandler() {
        super();
    }

    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for domain attribute");
        }
        if (value.trim().length() == 0) {
            throw new MalformedCookieException("Blank value for domain attribute");
        }
        cookie.setDomain(value);
    }

    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        String host = origin.getHost();
        final String domain = cookie.getDomain();
        if (domain == null) {
            throw new CookieRestrictionViolationException("Cookie domain may not be null");
        }
        if (!domain.equals(host)) {
            int dotIndex = domain.indexOf('.');
            if (dotIndex == -1) {
                throw new CookieRestrictionViolationException("Domain attribute \""
                        + domain
                        + "\" does not match the host \""
                        + host + "\"");
            }
            // domain must start with dot
            if (!domain.startsWith(".")) {
                throw new CookieRestrictionViolationException("Domain attribute \""
                        + domain
                        + "\" violates RFC 2109: domain must start with a dot");
            }
            // domain must have at least one embedded dot
            dotIndex = domain.indexOf('.', 1);
            if (dotIndex < 0 || dotIndex == domain.length() - 1) {
                throw new CookieRestrictionViolationException("Domain attribute \""
                        + domain
                        + "\" violates RFC 2109: domain must contain an embedded dot");
            }
            host = host.toLowerCase(Locale.ENGLISH);
            if (!host.endsWith(domain)) {
                throw new CookieRestrictionViolationException(
                        "Illegal domain attribute \"" + domain
                                + "\". Domain of origin: \"" + host + "\"");
            }
            // host minus domain may not contain any dots
            final String hostWithoutDomain = host.substring(0, host.length() - domain.length());
            if (hostWithoutDomain.indexOf('.') != -1) {
                throw new CookieRestrictionViolationException("Domain attribute \""
                        + domain
                        + "\" violates RFC 2109: host minus domain may not contain any dots");
            }
        }
    }

    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final String host = origin.getHost();
        final String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }
        return host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain));
    }

}
