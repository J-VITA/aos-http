package m.vita.module.http.cookie;

import m.vita.module.http.header.HttpContext;

/**
 * Factory for {@link CookieSpec} implementations.
 *
 * @since 4.3
 */
public interface CookieSpecProvider {

    /**
     * Creates an instance of {@link CookieSpec}.
     *
     * @return auth scheme.
     */
    CookieSpec create(HttpContext context);

}
