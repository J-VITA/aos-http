package m.vita.module.http.cookie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.util.Args;

@NotThreadSafe // HashMap is not thread-safe
public abstract class AbstractCookieSpec implements CookieSpec {

    /**
     * Stores attribute name -> attribute handler mappings
     */
    private final Map<String, CookieAttributeHandler> attribHandlerMap;

    /**
     * Default constructor
     * */
    public AbstractCookieSpec() {
        super();
        this.attribHandlerMap = new HashMap<String, CookieAttributeHandler>(10);
    }

    public void registerAttribHandler(
            final String name, final CookieAttributeHandler handler) {
        Args.notNull(name, "Attribute name");
        Args.notNull(handler, "Attribute handler");
        this.attribHandlerMap.put(name, handler);
    }

    /**
     * Finds an attribute handler {@link CookieAttributeHandler} for the
     * given attribute. Returns <tt>null</tt> if no attribute handler is
     * found for the specified attribute.
     *
     * @param name attribute name. e.g. Domain, Path, etc.
     * @return an attribute handler or <tt>null</tt>
     */
    protected CookieAttributeHandler findAttribHandler(final String name) {
        return this.attribHandlerMap.get(name);
    }

    /**
     * Gets attribute handler {@link CookieAttributeHandler} for the
     * given attribute.
     *
     * @param name attribute name. e.g. Domain, Path, etc.
     * @throws IllegalStateException if handler not found for the
     *          specified attribute.
     */
    protected CookieAttributeHandler getAttribHandler(final String name) {
        final CookieAttributeHandler handler = findAttribHandler(name);
        if (handler == null) {
            throw new IllegalStateException("Handler not registered for " +
                    name + " attribute.");
        } else {
            return handler;
        }
    }

    protected Collection<CookieAttributeHandler> getAttribHandlers() {
        return this.attribHandlerMap.values();
    }

}
