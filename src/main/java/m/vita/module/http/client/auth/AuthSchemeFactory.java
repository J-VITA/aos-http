package m.vita.module.http.client.auth;

import m.vita.module.http.header.HttpParams;

public interface AuthSchemeFactory {

    /**
     * Creates an instance of {@link AuthScheme} using given HTTP parameters.
     *
     * @param params HTTP parameters.
     *
     * @return auth scheme.
     */
    AuthScheme newInstance(HttpParams params);

}
