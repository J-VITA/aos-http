package m.vita.module.http.client.impl.client;

import java.util.Collection;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.auth.AUTH;
import m.vita.module.http.config.RequestConfig;
import m.vita.module.http.util.HttpStatus;

@Immutable
public class TargetAuthenticationStrategy extends AuthenticationStrategyImpl {

    public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();

    public TargetAuthenticationStrategy() {
        super(HttpStatus.SC_UNAUTHORIZED, AUTH.WWW_AUTH);
    }

    @Override
    Collection<String> getPreferredAuthSchemes(final RequestConfig config) {
        return config.getTargetPreferredAuthSchemes();
    }

}
