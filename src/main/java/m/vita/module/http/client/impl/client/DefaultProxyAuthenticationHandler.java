package m.vita.module.http.client.impl.client;

import java.util.List;
import java.util.Map;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.auth.AUTH;
import m.vita.module.http.client.auth.params.AuthPNames;
import m.vita.module.http.exception.MalformedChallengeException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HttpStatus;

@Immutable
public class DefaultProxyAuthenticationHandler extends AbstractAuthenticationHandler {

    public DefaultProxyAuthenticationHandler() {
        super();
    }

    public boolean isAuthenticationRequested(
            final HttpResponse response,
            final HttpContext context) {
        Args.notNull(response, "HTTP response");
        final int status = response.getStatusLine().getStatusCode();
        return status == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED;
    }

    public Map<String, Header> getChallenges(
            final HttpResponse response,
            final HttpContext context) throws MalformedChallengeException {
        Args.notNull(response, "HTTP response");
        final Header[] headers = response.getHeaders(AUTH.PROXY_AUTH);
        return parseChallenges(headers);
    }

    @Override
    protected List<String> getAuthPreferences(
            final HttpResponse response,
            final HttpContext context) {
        @SuppressWarnings("unchecked")
        final
        List<String> authpref = (List<String>) response.getParams().getParameter(
                AuthPNames.PROXY_AUTH_PREF);
        if (authpref != null) {
            return authpref;
        } else {
            return super.getAuthPreferences(response, context);
        }
    }

}
