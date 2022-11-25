package m.vita.module.http.client.auth;


import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.connect.AuthenticationStrategy;
import m.vita.module.http.exception.AuthenticationException;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;
import m.vita.module.http.util.Asserts;
import m.vita.module.http.util.HttpClientAndroidLog;

public class HttpAuthenticator {

    public HttpClientAndroidLog log;

    public HttpAuthenticator(final HttpClientAndroidLog log) {
        super();
        this.log = log != null ? log : new HttpClientAndroidLog(getClass());
    }

    public HttpAuthenticator() {
        this(null);
    }

    public boolean isAuthenticationRequested(
            final HttpHost host,
            final HttpResponse response,
            final AuthenticationStrategy authStrategy,
            final AuthState authState,
            final HttpContext context) {
        if (authStrategy.isAuthenticationRequested(host, response, context)) {
            this.log.debug("Authentication required");
            if (authState.getState() == AuthProtocolState.SUCCESS) {
                authStrategy.authFailed(host, authState.getAuthScheme(), context);
            }
            return true;
        } else {
            switch (authState.getState()) {
                case CHALLENGED:
                case HANDSHAKE:
                    this.log.debug("Authentication succeeded");
                    authState.setState(AuthProtocolState.SUCCESS);
                    authStrategy.authSucceeded(host, authState.getAuthScheme(), context);
                    break;
                case SUCCESS:
                    break;
                default:
                    authState.setState(AuthProtocolState.UNCHALLENGED);
            }
            return false;
        }
    }

    public boolean handleAuthChallenge(
            final HttpHost host,
            final HttpResponse response,
            final AuthenticationStrategy authStrategy,
            final AuthState authState,
            final HttpContext context) {
        try {
            if (this.log.isDebugEnabled()) {
                this.log.debug(host.toHostString() + " requested authentication");
            }
            final Map<String, Header> challenges = authStrategy.getChallenges(host, response, context);
            if (challenges.isEmpty()) {
                this.log.debug("Response contains no authentication challenges");
                return false;
            }

            final AuthScheme authScheme = authState.getAuthScheme();
            switch (authState.getState()) {
                case FAILURE:
                    return false;
                case SUCCESS:
                    authState.reset();
                    break;
                case CHALLENGED:
                case HANDSHAKE:
                    if (authScheme == null) {
                        this.log.debug("Auth scheme is null");
                        authStrategy.authFailed(host, null, context);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                case UNCHALLENGED:
                    if (authScheme != null) {
                        final String id = authScheme.getSchemeName();
                        final Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));
                        if (challenge != null) {
                            this.log.debug("Authorization challenge processed");
                            authScheme.processChallenge(challenge);
                            if (authScheme.isComplete()) {
                                this.log.debug("Authentication failed");
                                authStrategy.authFailed(host, authState.getAuthScheme(), context);
                                authState.reset();
                                authState.setState(AuthProtocolState.FAILURE);
                                return false;
                            } else {
                                authState.setState(AuthProtocolState.HANDSHAKE);
                                return true;
                            }
                        } else {
                            authState.reset();
                            // Retry authentication with a different scheme
                        }
                    }
            }
            final Queue<AuthOption> authOptions = authStrategy.select(challenges, host, response, context);
            if (authOptions != null && !authOptions.isEmpty()) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Selected authentication options: " + authOptions);
                }
                authState.setState(AuthProtocolState.CHALLENGED);
                authState.update(authOptions);
                return true;
            } else {
                return false;
            }
        } catch (final MalformedChallengeException ex) {
            if (this.log.isWarnEnabled()) {
                this.log.warn("Malformed challenge: " +  ex.getMessage());
            }
            authState.reset();
            return false;
        }
    }

    public void generateAuthResponse(
            final HttpRequest request,
            final AuthState authState,
            final HttpContext context) throws HttpException, IOException {
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
                            final Header header = doAuth(authScheme, creds, request, context);
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
                final Header header = doAuth(authScheme, creds, request, context);
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

    @SuppressWarnings("deprecation")
    private Header doAuth(
            final AuthScheme authScheme,
            final Credentials creds,
            final HttpRequest request,
            final HttpContext context) throws AuthenticationException {
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme) authScheme).authenticate(creds, request, context);
        } else {
            return authScheme.authenticate(creds, request);
        }
    }

}
