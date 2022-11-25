package m.vita.module.http.client.auth;

import m.vita.module.http.exception.AuthenticationException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;

public interface ContextAwareAuthScheme extends AuthScheme {

    /**
     * Produces an authorization string for the given set of
     * {@link Credentials}.
     *
     * @param credentials The set of credentials to be used for athentication
     * @param request The request being authenticated
     * @param context HTTP context
     * @throws AuthenticationException if authorization string cannot
     *   be generated due to an authentication failure
     *
     * @return the authorization string
     */
    Header authenticate(
            Credentials credentials,
            HttpRequest request,
            HttpContext context) throws AuthenticationException;

}
