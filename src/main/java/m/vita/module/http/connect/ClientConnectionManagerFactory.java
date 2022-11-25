package m.vita.module.http.connect;

import m.vita.module.http.factory.SchemeRegistry;
import m.vita.module.http.header.ClientConnectionManager;
import m.vita.module.http.header.HttpParams;

public interface ClientConnectionManagerFactory {

    ClientConnectionManager newInstance(
            HttpParams params,
            SchemeRegistry schemeRegistry);

}
