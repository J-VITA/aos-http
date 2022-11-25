package m.vita.module.http.cookie;

import java.util.Collection;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.cookie.param.CookieSpecPNames;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;

@Immutable
public class RFC2965SpecFactory implements CookieSpecFactory, CookieSpecProvider {

    private final String[] datepatterns;
    private final boolean oneHeader;

    public RFC2965SpecFactory(final String[] datepatterns, final boolean oneHeader) {
        super();
        this.datepatterns = datepatterns;
        this.oneHeader = oneHeader;
    }

    public RFC2965SpecFactory() {
        this(null, false);
    }

    public CookieSpec newInstance(final HttpParams params) {
        if (params != null) {

            String[] patterns = null;
            final Collection<?> param = (Collection<?>) params.getParameter(
                    CookieSpecPNames.DATE_PATTERNS);
            if (param != null) {
                patterns = new String[param.size()];
                patterns = param.toArray(patterns);
            }
            final boolean singleHeader = params.getBooleanParameter(
                    CookieSpecPNames.SINGLE_COOKIE_HEADER, false);

            return new RFC2965Spec(patterns, singleHeader);
        } else {
            return new RFC2965Spec();
        }
    }

    public CookieSpec create(final HttpContext context) {
        return new RFC2965Spec(this.datepatterns, this.oneHeader);
    }

}
