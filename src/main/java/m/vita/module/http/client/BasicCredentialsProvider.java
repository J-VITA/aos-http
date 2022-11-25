package m.vita.module.http.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.auth.AuthScope;
import m.vita.module.http.client.auth.Credentials;
import m.vita.module.http.util.Args;

@ThreadSafe
public class BasicCredentialsProvider implements CredentialsProvider {

    private final ConcurrentHashMap<AuthScope, Credentials> credMap;

    /**
     * Default constructor.
     */
    public BasicCredentialsProvider() {
        super();
        this.credMap = new ConcurrentHashMap<AuthScope, Credentials>();
    }

    public void setCredentials(
            final AuthScope authscope,
            final Credentials credentials) {
        Args.notNull(authscope, "Authentication scope");
        credMap.put(authscope, credentials);
    }

    /**
     * Find matching {@link Credentials credentials} for the given authentication scope.
     *
     * @param map the credentials hash map
     * @param authscope the {@link AuthScope authentication scope}
     * @return the credentials
     *
     */
    private static Credentials matchCredentials(
            final Map<AuthScope, Credentials> map,
            final AuthScope authscope) {
        // see if we get a direct hit
        Credentials creds = map.get(authscope);
        if (creds == null) {
            // Nope.
            // Do a full scan
            int bestMatchFactor  = -1;
            AuthScope bestMatch  = null;
            for (final AuthScope current: map.keySet()) {
                final int factor = authscope.match(current);
                if (factor > bestMatchFactor) {
                    bestMatchFactor = factor;
                    bestMatch = current;
                }
            }
            if (bestMatch != null) {
                creds = map.get(bestMatch);
            }
        }
        return creds;
    }

    public Credentials getCredentials(final AuthScope authscope) {
        Args.notNull(authscope, "Authentication scope");
        return matchCredentials(this.credMap, authscope);
    }

    public void clear() {
        this.credMap.clear();
    }

    @Override
    public String toString() {
        return credMap.toString();
    }

}
