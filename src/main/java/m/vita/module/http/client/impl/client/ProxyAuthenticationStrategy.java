package m.vita.module.http.client.impl.client;

import java.util.Collection;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.auth.AUTH;
import m.vita.module.http.config.RequestConfig;
import m.vita.module.http.util.HttpStatus;

@Immutable
public class ProxyAuthenticationStrategy extends AuthenticationStrategyImpl {

    public static final ProxyAuthenticationStrategy INSTANCE = new ProxyAuthenticationStrategy();

    public ProxyAuthenticationStrategy() {
        super(HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED, AUTH.PROXY_AUTH);
    }

    @Override
    Collection<String> getPreferredAuthSchemes(final RequestConfig config) {
        return config.getProxyPreferredAuthSchemes();
    }

}
