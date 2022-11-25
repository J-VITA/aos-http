package m.vita.module.http.connect;

import java.util.Map;
import java.util.Queue;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.client.auth.AuthOption;
import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;

public interface AuthenticationStrategy {

    /**
     * Determines if the given HTTP response response represents
     * an authentication challenge that was sent back as a result
     * of authentication failure.
     *
     * @param authhost authentication host.
     * @param response HTTP response.
     * @param context HTTP context.
     * @return <code>true</code> if user authentication is required,
     *   <code>false</code> otherwise.
     */
    boolean isAuthenticationRequested(
            HttpHost authhost,
            HttpResponse response,
            HttpContext context);

    /**
     * Extracts from the given HTTP response a collection of authentication
     * challenges, each of which represents an authentication scheme supported
     * by the authentication host.
     *
     * @param authhost authentication host.
     * @param response HTTP response.
     * @param context HTTP context.
     * @return a collection of challenges keyed by names of corresponding
     * authentication schemes.
     * @throws MalformedChallengeException if one of the authentication
     *  challenges is not valid or malformed.
     */
    Map<String, Header> getChallenges(
            HttpHost authhost,
            HttpResponse response,
            HttpContext context) throws MalformedChallengeException;

    /**
     * Selects one authentication challenge out of all available and
     * creates and generates {@link AuthOption} instance capable of
     * processing that challenge.
     *
     * @param challenges collection of challenges.
     * @param authhost authentication host.
     * @param response HTTP response.
     * @param context HTTP context.
     * @return authentication auth schemes that can be used for authentication. Can be empty.
     * @throws MalformedChallengeException if one of the authentication
     *  challenges is not valid or malformed.
     */
    Queue<AuthOption> select(
            Map<String, Header> challenges,
            HttpHost authhost,
            HttpResponse response,
            HttpContext context) throws MalformedChallengeException;

    /**
     * Callback invoked in case of successful authentication.
     *
     * @param authhost authentication host.
     * @param authScheme authentication scheme used.
     * @param context HTTP context.
     */
    void authSucceeded(
            HttpHost authhost,
            AuthScheme authScheme,
            HttpContext context);

    /**
     * Callback invoked in case of unsuccessful authentication.
     *
     * @param authhost authentication host.
     * @param authScheme authentication scheme used.
     * @param context HTTP context.
     */
    void authFailed(
            HttpHost authhost,
            AuthScheme authScheme,
            HttpContext context);

}
