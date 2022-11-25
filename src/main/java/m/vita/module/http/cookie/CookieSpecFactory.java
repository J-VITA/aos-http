package m.vita.module.http.cookie;

import m.vita.module.http.header.HttpParams;

public interface CookieSpecFactory {

    /**
     * Creates an instance of {@link CookieSpec} using given HTTP parameters.
     *
     * @param params HTTP parameters.
     *
     * @return cookie spec.
     */
    CookieSpec newInstance(HttpParams params);

}
