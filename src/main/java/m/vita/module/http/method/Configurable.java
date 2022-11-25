package m.vita.module.http.method;

import m.vita.module.http.config.RequestConfig;

public interface Configurable {

    /**
     * Returns actual request configuration.
     */
    RequestConfig getConfig();

}
