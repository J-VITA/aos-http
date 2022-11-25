package m.vita.module.http.client.impl;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.connect.ConnectionReuseStrategy;
import m.vita.module.http.header.HttpContext;

@Immutable
public class NoConnectionReuseStrategy implements ConnectionReuseStrategy {

    public static final NoConnectionReuseStrategy INSTANCE = new NoConnectionReuseStrategy();

    public NoConnectionReuseStrategy() {
        super();
    }

    public boolean keepAlive(final HttpResponse response, final HttpContext context) {
        return false;
    }

}
