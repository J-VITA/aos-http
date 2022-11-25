package m.vita.module.http.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.client.Scheme;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.util.Args;

@ThreadSafe
public final class SchemeRegistry {

    /** The available schemes in this registry. */
    private final ConcurrentHashMap<String, Scheme> registeredSchemes;

    /**
     * Creates a new, empty scheme registry.
     */
    public SchemeRegistry() {
        super();
        registeredSchemes = new ConcurrentHashMap<String,Scheme>();
    }

    /**
     * Obtains a scheme by name.
     *
     * @param name      the name of the scheme to look up (in lowercase)
     *
     * @return  the scheme, never <code>null</code>
     *
     * @throws IllegalStateException
     *          if the scheme with the given name is not registered
     */
    public final Scheme getScheme(final String name) {
        final Scheme found = get(name);
        if (found == null) {
            throw new IllegalStateException
                    ("Scheme '"+name+"' not registered.");
        }
        return found;
    }

    /**
     * Obtains the scheme for a host.
     * Convenience method for <code>getScheme(host.getSchemeName())</pre>
     *
     * @param host      the host for which to obtain the scheme
     *
     * @return  the scheme for the given host, never <code>null</code>
     *
     * @throws IllegalStateException
     *          if a scheme with the respective name is not registered
     */
    public final Scheme getScheme(final HttpHost host) {
        Args.notNull(host, "Host");
        return getScheme(host.getSchemeName());
    }

    /**
     * Obtains a scheme by name, if registered.
     *
     * @param name      the name of the scheme to look up (in lowercase)
     *
     * @return  the scheme, or
     *          <code>null</code> if there is none by this name
     */
    public final Scheme get(final String name) {
        Args.notNull(name, "Scheme name");
        // leave it to the caller to use the correct name - all lowercase
        //name = name.toLowerCase(Locale.ENGLISH);
        final Scheme found = registeredSchemes.get(name);
        return found;
    }

    /**
     * Registers a scheme.
     * The scheme can later be retrieved by its name
     * using {@link #getScheme(String) getScheme} or {@link #get get}.
     *
     * @param sch       the scheme to register
     *
     * @return  the scheme previously registered with that name, or
     *          <code>null</code> if none was registered
     */
    public final Scheme register(final Scheme sch) {
        Args.notNull(sch, "Scheme");
        final Scheme old = registeredSchemes.put(sch.getName(), sch);
        return old;
    }

    /**
     * Unregisters a scheme.
     *
     * @param name      the name of the scheme to unregister (in lowercase)
     *
     * @return  the unregistered scheme, or
     *          <code>null</code> if there was none
     */
    public final Scheme unregister(final String name) {
        Args.notNull(name, "Scheme name");
        // leave it to the caller to use the correct name - all lowercase
        //name = name.toLowerCase(Locale.ENGLISH);
        final Scheme gone = registeredSchemes.remove(name);
        return gone;
    }

    /**
     * Obtains the names of the registered schemes.
     *
     * @return  List containing registered scheme names.
     */
    public final List<String> getSchemeNames() {
        return new ArrayList<String>(registeredSchemes.keySet());
    }

    /**
     * Populates the internal collection of registered {@link Scheme protocol schemes}
     * with the content of the map passed as a parameter.
     *
     * @param map protocol schemes
     */
    public void setItems(final Map<String, Scheme> map) {
        if (map == null) {
            return;
        }
        registeredSchemes.clear();
        registeredSchemes.putAll(map);
    }

}

