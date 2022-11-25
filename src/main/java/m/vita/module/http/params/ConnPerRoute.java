package m.vita.module.http.params;

import m.vita.module.http.header.HttpRoute;

public interface ConnPerRoute {

    int getMaxForRoute(HttpRoute route);

}
