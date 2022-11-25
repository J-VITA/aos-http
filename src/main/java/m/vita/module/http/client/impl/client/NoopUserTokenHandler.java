package m.vita.module.http.client.impl.client;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.client.UserTokenHandler;
import m.vita.module.http.header.HttpContext;

@Immutable
public class NoopUserTokenHandler implements UserTokenHandler {

    public static final NoopUserTokenHandler INSTANCE = new NoopUserTokenHandler();

    public Object getUserToken(final HttpContext context) {
        return null;
    }

}
