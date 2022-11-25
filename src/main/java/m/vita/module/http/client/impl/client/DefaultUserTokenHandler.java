package m.vita.module.http.client.impl.client;

import java.security.Principal;

import javax.net.ssl.SSLSession;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.UserTokenHandler;
import m.vita.module.http.client.auth.AuthScheme;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.client.auth.Credentials;
import m.vita.module.http.connect.HttpConnection;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.ManagedHttpClientConnection;
import m.vita.module.http.client.protocol.HttpClientContext;

@Immutable
public class DefaultUserTokenHandler implements UserTokenHandler {

    public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();

    public Object getUserToken(final HttpContext context) {

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        Principal userPrincipal = null;

        final AuthState targetAuthState = clientContext.getTargetAuthState();
        if (targetAuthState != null) {
            userPrincipal = getAuthPrincipal(targetAuthState);
            if (userPrincipal == null) {
                final AuthState proxyAuthState = clientContext.getProxyAuthState();
                userPrincipal = getAuthPrincipal(proxyAuthState);
            }
        }

        if (userPrincipal == null) {
            final HttpConnection conn = clientContext.getConnection();
            if (conn.isOpen() && conn instanceof ManagedHttpClientConnection) {
                final SSLSession sslsession = ((ManagedHttpClientConnection) conn).getSSLSession();
                if (sslsession != null) {
                    userPrincipal = sslsession.getLocalPrincipal();
                }
            }
        }

        return userPrincipal;
    }

    private static Principal getAuthPrincipal(final AuthState authState) {
        final AuthScheme scheme = authState.getAuthScheme();
        if (scheme != null && scheme.isComplete() && scheme.isConnectionBased()) {
            final Credentials creds = authState.getCredentials();
            if (creds != null) {
                return creds.getUserPrincipal();
            }
        }
        return null;
    }

}
