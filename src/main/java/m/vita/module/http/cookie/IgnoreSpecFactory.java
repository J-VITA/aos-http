package m.vita.module.http.cookie;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;

@Immutable
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    public IgnoreSpecFactory() {
        super();
    }

    public CookieSpec newInstance(final HttpParams params) {
        return new IgnoreSpec();
    }

    public CookieSpec create(final HttpContext context) {
        return new IgnoreSpec();
    }

}
