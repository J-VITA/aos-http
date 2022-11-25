package m.vita.module.http.client.protocol;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.interceptor.HttpRequestInterceptor;
import m.vita.module.http.params.CoreProtocolPNames;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HTTP;

@Immutable
public class RequestUserAgent implements HttpRequestInterceptor {

    private final String userAgent;

    public RequestUserAgent(final String userAgent) {
        super();
        this.userAgent = userAgent;
    }

    public RequestUserAgent() {
        this(null);
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (!request.containsHeader(HTTP.USER_AGENT)) {
            String s = null;
            final HttpParams params = request.getParams();
            if (params != null) {
                s = (String) params.getParameter(CoreProtocolPNames.USER_AGENT);
            }
            if (s == null) {
                s = this.userAgent;
            }
            if (s != null) {
                request.addHeader(HTTP.USER_AGENT, s);
            }
        }
    }

}
