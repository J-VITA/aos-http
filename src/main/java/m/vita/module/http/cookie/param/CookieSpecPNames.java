package m.vita.module.http.cookie.param;

public interface CookieSpecPNames {

    /**
     * Defines valid date patterns to be used for parsing non-standard
     * <code>expires</code> attribute. Only required for compatibility
     * with non-compliant servers that still use <code>expires</code>
     * defined in the Netscape draft instead of the standard
     * <code>max-age</code> attribute.
     * <p>
     * This parameter expects a value of type {@link java.util.Collection}.
     * The collection elements must be of type {@link String} compatible
     * with the syntax of {@link java.text.SimpleDateFormat}.
     * </p>
     */
    public static final String DATE_PATTERNS = "http.protocol.cookie-datepatterns";

    /**
     * Defines whether cookies should be forced into a single
     * <code>Cookie</code> request header. Otherwise, each cookie is formatted
     * as a separate <code>Cookie</code> header.
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String SINGLE_COOKIE_HEADER = "http.protocol.single-cookie-header";

}
