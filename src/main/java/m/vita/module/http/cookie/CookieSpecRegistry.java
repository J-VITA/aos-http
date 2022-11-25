package m.vita.module.http.cookie;

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
public final class CookieSpecRegistry implements Lookup<CookieSpecProvider> {

    private final ConcurrentHashMap<String,CookieSpecFactory> registeredSpecs;

    public CookieSpecRegistry() {
        super();
        this.registeredSpecs = new ConcurrentHashMap<String,CookieSpecFactory>();
    }

    /**
     * Registers a {@link CookieSpecFactory} with the given identifier.
     * If a specification with the given name already exists it will be overridden.
     * This nameis the same one used to retrieve the {@link CookieSpecFactory}
     * from {@link #getCookieSpec(String)}.
     *
     * @param name the identifier for this specification
     * @param factory the {@link CookieSpecFactory} class to register
     *
     * @see #getCookieSpec(String)
     */
    public void register(final String name, final CookieSpecFactory factory) {
        Args.notNull(name, "Name");
        Args.notNull(factory, "Cookie spec factory");
        registeredSpecs.put(name.toLowerCase(Locale.ENGLISH), factory);
    }

    /**
     * Unregisters the {@link CookieSpecFactory} with the given ID.
     *
     * @param id the identifier of the {@link CookieSpec cookie specification} to unregister
     */
    public void unregister(final String id) {
        Args.notNull(id, "Id");
        registeredSpecs.remove(id.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Gets the {@link CookieSpec cookie specification} with the given ID.
     *
     * @param name the {@link CookieSpec cookie specification} identifier
     * @param params the {@link HttpParams HTTP parameters} for the cookie
     *  specification.
     *
     * @return {@link CookieSpec cookie specification}
     *
     * @throws IllegalStateException if a policy with the given name cannot be found
     */
    public CookieSpec getCookieSpec(final String name, final HttpParams params)
            throws IllegalStateException {

        Args.notNull(name, "Name");
        final CookieSpecFactory factory = registeredSpecs.get(name.toLowerCase(Locale.ENGLISH));
        if (factory != null) {
            return factory.newInstance(params);
        } else {
            throw new IllegalStateException("Unsupported cookie spec: " + name);
        }
    }

    /**
     * Gets the {@link CookieSpec cookie specification} with the given name.
     *
     * @param name the {@link CookieSpec cookie specification} identifier
     *
     * @return {@link CookieSpec cookie specification}
     *
     * @throws IllegalStateException if a policy with the given name cannot be found
     */
    public CookieSpec getCookieSpec(final String name)
            throws IllegalStateException {
        return getCookieSpec(name, null);
    }

    /**
     * Obtains a list containing the names of all registered {@link CookieSpec cookie
     * specs}.
     *
     * Note that the DEFAULT policy (if present) is likely to be the same
     * as one of the other policies, but does not have to be.
     *
     * @return list of registered cookie spec names
     */
    public List<String> getSpecNames(){
        return new ArrayList<String>(registeredSpecs.keySet());
    }

    /**
     * Populates the internal collection of registered {@link CookieSpec cookie
     * specs} with the content of the map passed as a parameter.
     *
     * @param map cookie specs
     */
    public void setItems(final Map<String, CookieSpecFactory> map) {
        if (map == null) {
            return;
        }
        registeredSpecs.clear();
        registeredSpecs.putAll(map);
    }

    public CookieSpecProvider lookup(final String name) {
        return new CookieSpecProvider() {

            public CookieSpec create(final HttpContext context) {
                final HttpRequest request = (HttpRequest) context.getAttribute(
                        ExecutionContext.HTTP_REQUEST);
                return getCookieSpec(name, request.getParams());
            }

        };
    }

}
