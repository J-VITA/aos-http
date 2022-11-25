package m.vita.module.http.cookie;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.MalformedCookieException;

@Immutable
public abstract class AbstractCookieAttributeHandler implements CookieAttributeHandler {

    public void validate(final Cookie cookie, final CookieOrigin origin)
            throws MalformedCookieException {
        // Do nothing
    }

    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        // Always match
        return true;
    }

}
