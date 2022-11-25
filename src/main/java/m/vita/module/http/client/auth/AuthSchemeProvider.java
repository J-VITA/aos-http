package m.vita.module.http.client.auth;

import m.vita.module.http.header.HttpContext;

/**
 * Factory for {@link AuthScheme} implementations.
 *
 * @since 4.3
 */
public interface AuthSchemeProvider {

    /**
     * Creates an instance of {@link AuthScheme}.
     *
     * @return auth scheme.
     */
    AuthScheme create(HttpContext context);

}
