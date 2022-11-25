package m.vita.module.http.cookie;

import java.util.Date;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.util.Args;

@Immutable
public class BasicMaxAgeHandler extends AbstractCookieAttributeHandler {

    public BasicMaxAgeHandler() {
        super();
    }

    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (value == null) {
            throw new MalformedCookieException("Missing value for max-age attribute");
        }
        final int age;
        try {
            age = Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new MalformedCookieException ("Invalid max-age attribute: "
                    + value);
        }
        if (age < 0) {
            throw new MalformedCookieException ("Negative max-age attribute: "
                    + value);
        }
        cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
    }

}
