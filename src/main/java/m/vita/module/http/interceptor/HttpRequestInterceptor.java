package m.vita.module.http.interceptor;

import java.io.IOException;

import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpRequest;

public interface HttpRequestInterceptor {
    void process(HttpRequest request, HttpContext context)
            throws HttpException, IOException;
}
