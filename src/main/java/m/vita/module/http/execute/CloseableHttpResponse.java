package m.vita.module.http.execute;

import java.io.Closeable;

import m.vita.module.http.HttpResponse;

public interface CloseableHttpResponse extends HttpResponse, Closeable {
}

