package m.vita.module.http.method;

import java.net.URI;

import m.vita.module.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpHead extends HttpRequestBase {

    public final static String METHOD_NAME = "HEAD";

    public HttpHead() {
        super();
    }

    public HttpHead(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpHead(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}
