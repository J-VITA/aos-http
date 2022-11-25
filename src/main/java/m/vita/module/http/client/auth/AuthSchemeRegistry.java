package m.vita.module.http.client.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.config.Lookup;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.client.protocol.ExecutionContext;
import m.vita.module.http.util.Args;

@ThreadSafe
public final class AuthSchemeRegistry implements Lookup<AuthSchemeProvider> {

    private final ConcurrentHashMap<String,AuthSchemeFactory> registeredSchemes;

    public AuthSchemeRegistry() {
        super();
        this.registeredSchemes = new ConcurrentHashMap<String,AuthSchemeFactory>();
    }

    /**
     * Registers a {@link AuthSchemeFactory} with  the given identifier. If a factory with the
     * given name already exists it will be overridden. This name is the same one used to
     * retrieve the {@link AuthScheme authentication scheme} from {@link #getAuthScheme}.
     *
     * <p>
     * Please note that custom authentication preferences, if used, need to be updated accordingly
     * for the new {@link AuthScheme authentication scheme} to take effect.
     * </p>
     *
     * @param name the identifier for this scheme
     * @param factory the {@link AuthSchemeFactory} class to register
     *
     * @see #getAuthScheme
     */
    public void register(
            final String name,
            final AuthSchemeFactory factory) {
        Args.notNull(name, "Name");
        Args.notNull(factory, "Authentication scheme factory");
        registeredSchemes.put(name.toLowerCase(Locale.ENGLISH), factory);
    }

    /**
     * Unregisters the class implementing an {@link AuthScheme authentication scheme} with
     * the given name.
     *
     * @param name the identifier of the class to unregister
     */
    public void unregister(final String name) {
        Args.notNull(name, "Name");
        registeredSchemes.remove(name.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Gets the {@link AuthScheme authentication scheme} with the given name.
     *
     * @param name the {@link AuthScheme authentication scheme} identifier
     * @param params the {@link HttpParams HTTP parameters} for the authentication
     *  scheme.
     *
     * @return {@link AuthScheme authentication scheme}
     *
     * @throws IllegalStateException if a scheme with the given name cannot be found
     */
    public AuthScheme getAuthScheme(final String name, final HttpParams params)
            throws IllegalStateException {

        Args.notNull(name, "Name");
        final AuthSchemeFactory factory = registeredSchemes.get(name.toLowerCase(Locale.ENGLISH));
        if (factory != null) {
            return factory.newInstance(params);
        } else {
            throw new IllegalStateException("Unsupported authentication scheme: " + name);
        }
    }

    /**
     * Obtains a list containing the names of all registered {@link AuthScheme authentication
     * schemes}
     *
     * @return list of registered scheme names
     */
    public List<String> getSchemeNames() {
        return new ArrayList<String>(registeredSchemes.keySet());
    }

    /**
     * Populates the internal collection of registered {@link AuthScheme authentication schemes}
     * with the content of the map passed as a parameter.
     *
     * @param map authentication schemes
     */
    public void setItems(final Map<String, AuthSchemeFactory> map) {
        if (map == null) {
            return;
        }
        registeredSchemes.clear();
        registeredSchemes.putAll(map);
    }

    public AuthSchemeProvider lookup(final String name) {
        return new AuthSchemeProvider() {

            public AuthScheme create(final HttpContext context) {
                final HttpRequest request = (HttpRequest) context.getAttribute(
                        ExecutionContext.HTTP_REQUEST);
                return getAuthScheme(name, request.getParams());
            }

        };
    }

}
