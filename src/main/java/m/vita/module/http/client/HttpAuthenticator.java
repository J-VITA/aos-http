package m.vita.module.http.client;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.connect.AuthenticationStrategy;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.util.HttpClientAndroidLog;

public class HttpAuthenticator extends m.swc.http.http.client.auth.HttpAuthenticator {

    public HttpAuthenticator(final HttpClientAndroidLog log) {
        super(log);
    }

    public HttpAuthenticator() {
        super();
    }

    public boolean authenticate (
            final HttpHost host,
            final HttpResponse response,
            final AuthenticationStrategy authStrategy,
            final AuthState authState,
            final HttpContext context) {
        return handleAuthChallenge(host, response, authStrategy, authState, context);
    }

}