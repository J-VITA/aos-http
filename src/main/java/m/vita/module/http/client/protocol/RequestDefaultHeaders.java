package m.vita.module.http.client.protocol;

import java.io.IOException;
import java.util.Collection;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.interceptor.HttpRequestInterceptor;
import m.vita.module.http.params.ClientPNames;
import m.vita.module.http.util.Args;

@Immutable
public class RequestDefaultHeaders implements HttpRequestInterceptor {

    private final Collection<? extends Header> defaultHeaders;

    /**
     * @since 4.3
     */
    public RequestDefaultHeaders(final Collection<? extends Header> defaultHeaders) {
        super();
        this.defaultHeaders = defaultHeaders;
    }

    public RequestDefaultHeaders() {
        this(null);
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }

        // Add default headers
        @SuppressWarnings("unchecked")
        Collection<? extends Header> defHeaders = (Collection<? extends Header>)
                request.getParams().getParameter(ClientPNames.DEFAULT_HEADERS);
        if (defHeaders == null) {
            defHeaders = this.defaultHeaders;
        }

        if (defHeaders != null) {
            for (final Header defHeader : defHeaders) {
                request.addHeader(defHeader);
            }
        }
    }

}
