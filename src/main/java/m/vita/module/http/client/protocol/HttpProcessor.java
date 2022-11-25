package m.vita.module.http.client.protocol;

import m.vita.module.http.interceptor.HttpRequestInterceptor;
import m.vita.module.http.interceptor.HttpResponseInterceptor;

public interface HttpProcessor
        extends HttpRequestInterceptor, HttpResponseInterceptor {

    // no additional methods
}
