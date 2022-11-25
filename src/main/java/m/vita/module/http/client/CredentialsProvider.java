package m.vita.module.http.client;


import m.vita.module.http.client.auth.AuthScope;
import m.vita.module.http.client.auth.Credentials;

public interface CredentialsProvider {

    /**
     * Sets the {@link Credentials credentials} for the given authentication
     * scope. Any previous credentials for the given scope will be overwritten.
     *
     * @param authscope the {@link AuthScope authentication scope}
     * @param credentials the authentication {@link Credentials credentials}
     * for the given scope.
     *
     * @see #getCredentials(AuthScope)
     */
    void setCredentials(AuthScope authscope, Credentials credentials);

    /**
     * Get the {@link Credentials credentials} for the given authentication scope.
     *
     * @param authscope the {@link AuthScope authentication scope}
     * @return the credentials
     *
     * @see #setCredentials(AuthScope, Credentials)
     */
    Credentials getCredentials(AuthScope authscope);

    /**
     * Clears all credentials.
     */
    void clear();

}
