package m.vita.module.http.client;

import java.util.Map;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.exception.AuthenticationException;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;

public interface AuthenticationHandler {

    /**
     * Determines if the given HTTP response response represents
     * an authentication challenge that was sent back as a result
     * of authentication failure
     * @param response HTTP response.
     * @param context HTTP context.
     * @return <code>true</code> if user authentication is required,
     *   <code>false</code> otherwise.
     */
    boolean isAuthenticationRequested(
            HttpResponse response,
            HttpContext context);

    /**
     * Extracts from the given HTTP response a collection of authentication
     * challenges, each of which represents an authentication scheme supported
     * by the authentication host.
     *
     * @param response HTTP response.
     * @param context HTTP context.
     * @return a collection of challenges keyed by names of corresponding
     * authentication schemes.
     * @throws MalformedChallengeException if one of the authentication
     *  challenges is not valid or malformed.
     */
    Map<String, Header> getChallenges(
            HttpResponse response,
            HttpContext context) throws MalformedChallengeException;

    /**
     * Selects one authentication challenge out of all available and
     * creates and generates {@link AuthScheme} instance capable of
     * processing that challenge.
     * @param challenges collection of challenges.
     * @param response HTTP response.
     * @param context HTTP context.
     * @return authentication scheme to use for authentication.
     * @throws AuthenticationException if an authentication scheme
     *  could not be selected.
     */
    AuthScheme selectScheme(
            Map<String, Header> challenges,
            HttpResponse response,
            HttpContext context) throws AuthenticationException;

}
