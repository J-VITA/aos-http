package m.vita.module.http.client.auth;

import m.vita.module.http.header.HttpHost;

public interface AuthCache {

    void put(HttpHost host, AuthScheme authScheme);

    AuthScheme get(HttpHost host);

    void remove(HttpHost host);

    void clear();

}