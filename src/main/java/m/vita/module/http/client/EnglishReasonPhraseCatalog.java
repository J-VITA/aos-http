package m.vita.module.http.client;

import java.util.Locale;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HttpStatus;

@Immutable
public class EnglishReasonPhraseCatalog implements ReasonPhraseCatalog {

    // static array with english reason phrases defined below

    /**
     * The default instance of this catalog.
     * This catalog is thread safe, so there typically
     * is no need to create other instances.
     */
    public final static EnglishReasonPhraseCatalog INSTANCE =
            new EnglishReasonPhraseCatalog();


    /**
     * Restricted default constructor, for derived classes.
     * If you need an instance of this class, use {@link #INSTANCE INSTANCE}.
     */
    protected EnglishReasonPhraseCatalog() {
        // no body
    }


    /**
     * Obtains the reason phrase for a status code.
     *
     * @param status    the status code, in the range 100-599
     * @param loc       ignored
     *
     * @return  the reason phrase, or <code>null</code>
     */
    public String getReason(final int status, final Locale loc) {
        Args.check(status >= 100 && status < 600, "Unknown category for status code " + status);
        final int category = status / 100;
        final int subcode  = status - 100*category;

        String reason = null;
        if (REASON_PHRASES[category].length > subcode) {
            reason = REASON_PHRASES[category][subcode];
        }

        return reason;
    }


    /** Reason phrases lookup table. */
    private static final String[][] REASON_PHRASES = new String[][]{
            null,
            new String[3],  // 1xx
            new String[8],  // 2xx
            new String[8],  // 3xx
            new String[25], // 4xx
            new String[8]   // 5xx
    };



    /**
     * Stores the given reason phrase, by status code.
     * Helper method to initialize the static lookup table.
     *
     * @param status    the status code for which to define the phrase
     * @param reason    the reason phrase for this status code
     */
    private static void setReason(final int status, final String reason) {
        final int category = status / 100;
        final int subcode  = status - 100*category;
        REASON_PHRASES[category][subcode] = reason;
    }


    // ----------------------------------------------------- Static Initializer

    /** Set up status code to "reason phrase" map. */
    static {
        // HTTP 1.0 Server status codes -- see RFC 1945
        setReason(HttpStatus.SC_OK,
                "OK");
        setReason(HttpStatus.SC_CREATED,
                "Created");
        setReason(HttpStatus.SC_ACCEPTED,
                "Accepted");
        setReason(HttpStatus.SC_NO_CONTENT,
                "No Content");
        setReason(HttpStatus.SC_MOVED_PERMANENTLY,
                "Moved Permanently");
        setReason(HttpStatus.SC_MOVED_TEMPORARILY,
                "Moved Temporarily");
        setReason(HttpStatus.SC_NOT_MODIFIED,
                "Not Modified");
        setReason(HttpStatus.SC_BAD_REQUEST,
                "Bad Request");
        setReason(HttpStatus.SC_UNAUTHORIZED,
                "Unauthorized");
        setReason(HttpStatus.SC_FORBIDDEN,
                "Forbidden");
        setReason(HttpStatus.SC_NOT_FOUND,
                "Not Found");
        setReason(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                "Internal Server Error");
        setReason(HttpStatus.SC_NOT_IMPLEMENTED,
                "Not Implemented");
        setReason(HttpStatus.SC_BAD_GATEWAY,
                "Bad Gateway");
        setReason(HttpStatus.SC_SERVICE_UNAVAILABLE,
                "Service Unavailable");

        // HTTP 1.1 Server status codes -- see RFC 2048
        setReason(HttpStatus.SC_CONTINUE,
                "Continue");
        setReason(HttpStatus.SC_TEMPORARY_REDIRECT,
                "Temporary Redirect");
        setReason(HttpStatus.SC_METHOD_NOT_ALLOWED,
                "Method Not Allowed");
        setReason(HttpStatus.SC_CONFLICT,
                "Conflict");
        setReason(HttpStatus.SC_PRECONDITION_FAILED,
                "Precondition Failed");
        setReason(HttpStatus.SC_REQUEST_TOO_LONG,
                "Request Too Long");
        setReason(HttpStatus.SC_REQUEST_URI_TOO_LONG,
                "Request-URI Too Long");
        setReason(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE,
                "Unsupported Media Type");
        setReason(HttpStatus.SC_MULTIPLE_CHOICES,
                "Multiple Choices");
        setReason(HttpStatus.SC_SEE_OTHER,
                "See Other");
        setReason(HttpStatus.SC_USE_PROXY,
                "Use Proxy");
        setReason(HttpStatus.SC_PAYMENT_REQUIRED,
                "Payment Required");
        setReason(HttpStatus.SC_NOT_ACCEPTABLE,
                "Not Acceptable");
        setReason(HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED,
                "Proxy Authentication Required");
        setReason(HttpStatus.SC_REQUEST_TIMEOUT,
                "Request Timeout");

        setReason(HttpStatus.SC_SWITCHING_PROTOCOLS,
                "Switching Protocols");
        setReason(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION,
                "Non Authoritative Information");
        setReason(HttpStatus.SC_RESET_CONTENT,
                "Reset Content");
        setReason(HttpStatus.SC_PARTIAL_CONTENT,
                "Partial Content");
        setReason(HttpStatus.SC_GATEWAY_TIMEOUT,
                "Gateway Timeout");
        setReason(HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED,
                "Http Version Not Supported");
        setReason(HttpStatus.SC_GONE,
                "Gone");
        setReason(HttpStatus.SC_LENGTH_REQUIRED,
                "Length Required");
        setReason(HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE,
                "Requested Range Not Satisfiable");
        setReason(HttpStatus.SC_EXPECTATION_FAILED,
                "Expectation Failed");

        // WebDAV Server-specific status codes
        setReason(HttpStatus.SC_PROCESSING,
                "Processing");
        setReason(HttpStatus.SC_MULTI_STATUS,
                "Multi-Status");
        setReason(HttpStatus.SC_UNPROCESSABLE_ENTITY,
                "Unprocessable Entity");
        setReason(HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE,
                "Insufficient Space On Resource");
        setReason(HttpStatus.SC_METHOD_FAILURE,
                "Method Failure");
        setReason(HttpStatus.SC_LOCKED,
                "Locked");
        setReason(HttpStatus.SC_INSUFFICIENT_STORAGE,
                "Insufficient Storage");
        setReason(HttpStatus.SC_FAILED_DEPENDENCY,
                "Failed Dependency");
    }


}
