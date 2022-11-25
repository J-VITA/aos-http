package m.vita.module.http.client.protocol;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.auth.AUTH;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.header.HttpRoute;
import m.vita.module.http.header.HttpRoutedConnection;
import m.vita.module.http.util.Args;

@Immutable
public class RequestProxyAuthentication extends RequestAuthenticationBase {

    public RequestProxyAuthentication() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");

        if (request.containsHeader(AUTH.PROXY_AUTH_RESP)) {
            return;
        }

        final HttpRoutedConnection conn = (HttpRoutedConnection) context.getAttribute(
                ExecutionContext.HTTP_CONNECTION);
        if (conn == null) {
            this.log.debug("HTTP connection not set in the context");
            return;
        }
        final HttpRoute route = conn.getRoute();
        if (route.isTunnelled()) {
            return;
        }

        // Obtain authentication state
        final AuthState authState = (AuthState) context.getAttribute(
                ClientContext.PROXY_AUTH_STATE);
        if (authState == null) {
            this.log.debug("Proxy auth state not set in the context");
            return;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Proxy auth state: " + authState.getState());
        }
        process(authState, request, context);
    }

}