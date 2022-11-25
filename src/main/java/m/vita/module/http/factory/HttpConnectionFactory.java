package m.vita.module.http.factory;

import m.vita.module.http.config.ConnectionConfig;
import m.vita.module.http.connect.HttpConnection;

public interface HttpConnectionFactory<T, C extends HttpConnection> {

    C create(T route, ConnectionConfig config);

}
