package m.vita.module.http.interceptor;

import java.io.IOException;

import m.vita.module.http.client.CredentialsProvider;
import m.vita.module.http.client.auth.AuthScope;
import m.vita.module.http.client.auth.AuthState;
import m.vita.module.http.client.auth.BasicScheme;
import m.vita.module.http.client.auth.Credentials;
import m.vita.module.http.client.protocol.ClientContext;
import m.vita.module.http.client.protocol.ExecutionContext;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpHost;
import m.vita.module.http.header.HttpRequest;


public class PreemptiveAuthorizationHttpRequestInterceptor implements HttpRequestInterceptor {
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
        CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
                ClientContext.CREDS_PROVIDER);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

        if (authState.getAuthScheme() == null) {
            AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
            Credentials creds = credsProvider.getCredentials(authScope);
            if (creds != null) {
                authState.setAuthScheme(new BasicScheme());
                authState.setCredentials(creds);
            }
        }
    }
}
