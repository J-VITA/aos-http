package m.vita.module.http.interceptor;

import java.io.IOException;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;

public interface HttpResponseInterceptor {
    void process(HttpResponse response, HttpContext context)
            throws HttpException, IOException;
}
