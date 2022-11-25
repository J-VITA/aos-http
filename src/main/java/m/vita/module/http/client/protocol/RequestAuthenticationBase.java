package m.vita.module.http.client.protocol;

import java.util.Queue;

import m.vita.module.http.client.auth.AuthOption;
import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.client.auth.ContextAwareAuthScheme;
import m.vita.module.http.client.auth.Credentials;
import m.vita.module.http.exception.AuthenticationException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.interceptor.HttpRequestInterceptor;
import m.vita.module.http.util.Asserts;
import m.vita.module.http.util.HttpClientAndroidLog;

abstract class RequestAuthenticationBase implements HttpRequestInterceptor {

    final HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());

    public RequestAuthenticationBase() {
        super();
    }

    void process(
            final AuthState authState,
            final HttpRequest request,
            final HttpContext context) {
        AuthScheme authScheme = authState.getAuthScheme();
        Credentials creds = authState.getCredentials();
        switch (authState.getState()) {
            case FAILURE:
                return;
            case SUCCESS:
                ensureAuthScheme(authScheme);
                if (authScheme.isConnectionBased()) {
                    return;
                }
                break;
            case CHALLENGED:
                final Queue<AuthOption> authOptions = authState.getAuthOptions();
                if (authOptions != null) {
                    while (!authOptions.isEmpty()) {
                        final AuthOption authOption = authOptions.remove();
                        authScheme = authOption.getAuthScheme();
                        creds = authOption.getCredentials();
                        authState.update(authScheme, creds);
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Generating response to an authentication challenge using "
                                    + authScheme.getSchemeName() + " scheme");
                        }
                        try {
                            final Header header = authenticate(authScheme, creds, request, context);
                            request.addHeader(header);
                            break;
                        } catch (final AuthenticationException ex) {
                            if (this.log.isWarnEnabled()) {
                                this.log.warn(authScheme + " authentication error: " + ex.getMessage());
                            }
                        }
                    }
                    return;
                } else {
                    ensureAuthScheme(authScheme);
                }
        }
        if (authScheme != null) {
            try {
                final Header header = authenticate(authScheme, creds, request, context);
                request.addHeader(header);
            } catch (final AuthenticationException ex) {
                if (this.log.isErrorEnabled()) {
                    this.log.error(authScheme + " authentication error: " + ex.getMessage());
                }
            }
        }
    }

    private void ensureAuthScheme(final AuthScheme authScheme) {
        Asserts.notNull(authScheme, "Auth scheme");
    }

    private Header authenticate(
            final AuthScheme authScheme,
            final Credentials creds,
            final HttpRequest request,
            final HttpContext context) throws AuthenticationException {
        Asserts.notNull(authScheme, "Auth scheme");
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme) authScheme).authenticate(creds, request, context);
        } else {
            return authScheme.authenticate(creds, request);
        }
    }

}
