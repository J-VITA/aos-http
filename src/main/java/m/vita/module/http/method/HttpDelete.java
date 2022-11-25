package m.vita.module.http.method;

import java.net.URI;


/**
 * The current Android (API level 21) bundled version of the Apache Http Client does not implement
 * a HttpEntityEnclosingRequestBase type of HTTP DELETE method.
 * Until the Android version is updated this can serve in it's stead.
 * This implementation can and should go away when the official solution arrives.
 */
public final class HttpDelete extends HttpEntityEnclosingRequestBase {
    public final static String METHOD_NAME = "DELETE";

    public HttpDelete() {
        super();
    }

    /**
     * @param uri target url as URI
     */
    public HttpDelete(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @param uri target url as String
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
