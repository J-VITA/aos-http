package m.vita.module.http.client;

import java.net.URI;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.handler.JEBRedirectHandler;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpUriRequest;
import m.vita.module.http.method.HttpGet;
import m.vita.module.http.method.HttpHead;

@Immutable
class DefaultRedirectStrategyAdaptor implements RedirectStrategy {

    private final JEBRedirectHandler handler;

    public DefaultRedirectStrategyAdaptor(final JEBRedirectHandler handler) {
        super();
        this.handler = handler;
    }

    public boolean isRedirected(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws ProtocolException {
        return this.handler.isRedirectRequested(response, context);
    }

    public HttpUriRequest getRedirect(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws ProtocolException {
        final URI uri = this.handler.getLocationURI(response, context);
        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase(HttpHead.METHOD_NAME)) {
            return new HttpHead(uri);
        } else {
            return new HttpGet(uri);
        }
    }

    public RedirectHandler getHandler() {
        return this.handler;
    }

}
