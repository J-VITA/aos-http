package m.vita.module.http.cookie;

import java.util.Collection;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.cookie.param.CookieSpecPNames;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;

@Immutable
public class NetscapeDraftSpecFactory implements CookieSpecFactory, CookieSpecProvider {

    private final String[] datepatterns;

    public NetscapeDraftSpecFactory(final String[] datepatterns) {
        super();
        this.datepatterns = datepatterns;
    }

    public NetscapeDraftSpecFactory() {
        this(null);
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
            return new NetscapeDraftSpec(patterns);
        } else {
            return new NetscapeDraftSpec();
        }
    }

    public CookieSpec create(final HttpContext context) {
        return new NetscapeDraftSpec(this.datepatterns);
    }

}
