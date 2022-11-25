package m.vita.module.http.client.protocol;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.interceptor.HttpRequestInterceptor;

@Immutable
public class RequestAcceptEncoding implements HttpRequestInterceptor {

    /**
     * Adds the header {@code "Accept-Encoding: gzip,deflate"} to the request.
     */
    public void process(
            final HttpRequest request,
            final HttpContext context) throws HttpException, IOException {

        /* Signal support for Accept-Encoding transfer encodings. */
        if (!request.containsHeader("Accept-Encoding")) {
            request.addHeader("Accept-Encoding", "gzip,deflate");
        }
    }

}
