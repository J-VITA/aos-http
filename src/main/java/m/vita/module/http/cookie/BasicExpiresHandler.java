package m.vita.module.http.cookie;

import java.util.Date;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.DateUtils;

@Immutable
public class BasicExpiresHandler extends AbstractCookieAttributeHandler {

    /** Valid date patterns */
    private final String[] datepatterns;

    public BasicExpiresHandler(final String[] datepatterns) {
        Args.notNull(datepatterns, "Array of date patterns");
        this.datepatterns = datepatterns;
    }

    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for expires attribute");
        }
        final Date expiry = DateUtils.parseDate(value, this.datepatterns);
        if (expiry == null) {
            throw new MalformedCookieException("Unable to parse expires attribute: "
                    + value);
        }
        cookie.setExpiryDate(expiry);
    }

}
