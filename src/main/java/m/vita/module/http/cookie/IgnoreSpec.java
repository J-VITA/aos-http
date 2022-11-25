package m.vita.module.http.cookie;

import java.util.Collections;
import java.util.List;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.exception.MalformedCookieException;
import m.vita.module.http.header.Header;

@NotThreadSafe // superclass is @NotThreadSafe
public class IgnoreSpec extends CookieSpecBase {

    public int getVersion() {
        return 0;
    }

    public List<Cookie> parse(final Header header, final CookieOrigin origin)
            throws MalformedCookieException {
        return Collections.emptyList();
    }

    public List<Header> formatCookies(final List<Cookie> cookies) {
        return Collections.emptyList();
    }

    public Header getVersionHeader() {
        return null;
    }
}
