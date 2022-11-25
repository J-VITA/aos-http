package m.vita.module.http.cookie;

import java.util.Collection;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.cookie.param.CookieSpecPNames;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;

@Immutable
public class BestMatchSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    private final String[] datepatterns;
    private final boolean oneHeader;

    public BestMatchSpecFactory(final String[] datepatterns, final boolean oneHeader) {
        super();
        this.datepatterns = datepatterns;
        this.oneHeader = oneHeader;
    }

    public BestMatchSpecFactory() {
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

            return new BestMatchSpec(patterns, singleHeader);
        } else {
            return new BestMatchSpec();
        }
    }

    public CookieSpec create(final HttpContext context) {
        return new BestMatchSpec(this.datepatterns, this.oneHeader);
    }

}
