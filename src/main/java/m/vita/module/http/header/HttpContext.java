package m.vita.module.http.header;

public interface HttpContext {

    /** The prefix reserved for use by HTTP components. "http." */
    public static final String RESERVED_PREFIX  = "http.";

    /**
     * Obtains attribute with the given name.
     *
     * @param id the attribute name.
     * @return attribute value, or <code>null</code> if not set.
     */
    Object getAttribute(String id);

    /**
     * Sets value of the attribute with the given name.
     *
     * @param id the attribute name.
     * @param obj the attribute value.
     */
    void setAttribute(String id, Object obj);

    /**
     * Removes attribute with the given name from the context.
     *
     * @param id the attribute name.
     * @return attribute value, or <code>null</code> if not set.
     */
    Object removeAttribute(String id);

}
