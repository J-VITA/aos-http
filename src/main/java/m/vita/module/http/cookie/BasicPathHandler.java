package m.vita.module.http.cookie;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.TextUtils;

@Immutable
public class BasicPathHandler implements CookieAttributeHandler {

    public BasicPathHandler() {
        super();
    }

    public void parse(
            final SetCookie cookie, final String value) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        cookie.setPath(!TextUtils.isBlank(value) ? value : "/");
    }

    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        if (!match(cookie, origin)) {
            throw new CookieRestrictionViolationException(
                    "Illegal path attribute \"" + cookie.getPath()
                            + "\". Path of origin: \"" + origin.getPath() + "\"");
        }
    }

    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        final String targetpath = origin.getPath();
        String topmostPath = cookie.getPath();
        if (topmostPath == null) {
            topmostPath = "/";
        }
        if (topmostPath.length() > 1 && topmostPath.endsWith("/")) {
            topmostPath = topmostPath.substring(0, topmostPath.length() - 1);
        }
        boolean match = targetpath.startsWith (topmostPath);
        // if there is a match and these values are not exactly the same we have
        // to make sure we're not matcing "/foobar" and "/foo"
        if (match && targetpath.length() != topmostPath.length()) {
            if (!topmostPath.endsWith("/")) {
                match = (targetpath.charAt(topmostPath.length()) == '/');
            }
        }
        return match;
    }

}
