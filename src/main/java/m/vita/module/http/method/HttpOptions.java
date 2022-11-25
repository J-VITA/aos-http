package m.vita.module.http.method;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.header.HeaderIterator;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class HttpOptions extends HttpRequestBase {

    public final static String METHOD_NAME = "OPTIONS";

    public HttpOptions() {
        super();
    }

    public HttpOptions(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpOptions(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public Set<String> getAllowedMethods(final HttpResponse response) {
        Args.notNull(response, "HTTP response");

        final HeaderIterator it = response.headerIterator("Allow");
        final Set<String> methods = new HashSet<String>();
        while (it.hasNext()) {
            final Header header = it.nextHeader();
            final HeaderElement[] elements = header.getElements();
            for (final HeaderElement element : elements) {
                methods.add(element.getName());
            }
        }
        return methods;
    }

}
