package m.vita.module.http.method;

import java.net.URI;

public final class JEBHttpGet extends HttpEntityEnclosingRequestBase {

    public final static String METHOD_NAME = "GET";

    public JEBHttpGet() {
        super();
    }

    /**
     * @param uri target url as URI
     */
    public JEBHttpGet(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @param uri target url as String
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public JEBHttpGet(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
